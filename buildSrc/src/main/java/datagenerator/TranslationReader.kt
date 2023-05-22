import datagenerator.CPFilePath
import datagenerator.SupportedLanguage
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.gradle.api.resources.MissingResourceException
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList


private const val LANG_CODE = "LANG"
private const val ALPHA_2 = "COUNTRY_ALPHA2_CODE"
private const val TRANSLATION = "COUNTRY_NAME"
private const val NO_MATCH_MSG = "NoMatchMsg"
private const val SEARCH_HINT = "SearchHint"
private const val DIALOG_TITLE = "DialogTitle"
private const val CLEAR_SELECTION = "ClearSelection"
private const val SELECTION_PLACEHOLDER = "SelectionPlaceholder"
typealias LanguageCode = String

class TranslationReader(val dataGeneratorRootDir: String) {
    companion object{
        object Header {
            const val LANG_CODE = "LANG"
            const val ALPHA_2 = "COUNTRY_ALPHA2_CODE"
            const val ENGLISH_NAME = "ENGLISH_NAME"
            const val TRANSLATION = "COUNTRY_NAME"
            const val NO_MATCH_MSG = "NoMatchMsg"
            const val SEARCH_HINT = "SearchHint"
            const val DIALOG_TITLE = "DialogTitle"
            const val CLEAR_SELECTION = "ClearSelection"
            const val SELECTION_PLACEHOLDER = "SelectionPlaceholder"

        }
    }
    fun readAllTranslations(
        baseCountries: List<BaseCountry>,
        ip2locationTranslationFilePath: String = "$dataGeneratorRootDir/data/ip2location/IP2LOCATION-COUNTRY-MULTILINGUAL.CSV",
        translationDirPath: String = "$dataGeneratorRootDir/data/translations",
        messageTranslationFilePath: String = "$dataGeneratorRootDir/data/MultilingualCPMessages.CSV"
    ): List<LanguageTranslation> {
        val languageMap = mutableMapOf<LanguageCode, LanguageTranslation>()
        for (supportedLanguage in SupportedLanguage.values()) {
            languageMap[supportedLanguage.identifier] =
                LanguageTranslation(supportedLanguage, mutableMapOf())
        }

        val supportedLangCodes = languageMap.keys.map { it.toUpperCase() }
        val nameTranslations = getTranslationNames(
            baseCountries,
            ip2locationTranslationFilePath,
            translationDirPath,
            supportedLangCodes
        )
        val messageTranslations =
            getMessageTranslations(messageTranslationFilePath, supportedLangCodes)
        val errors = mutableListOf<String>()
        SupportedLanguage.values().forEach { language ->
            val languageCode = language.identifier.toUpperCase()
            var isErrorHeaderAdded = false
            val languageTranslationMap = nameTranslations[languageCode]
            baseCountries.forEach { baseCountry ->
                val alpha2 = baseCountry.alpha2
                val translation = languageTranslationMap?.get(alpha2)
                if (translation == null || translation.contains("_TODO", true)) {
                    if (isErrorHeaderAdded.not()) {
                        isErrorHeaderAdded = true
                        errors.add("\n\n\nMissing ${language.name}($languageCode) translation(s). Add following to translations/${languageCode}_translations.csv")
                    }
                    errors.add(""""$languageCode","${alpha2}","${baseCountry.englishName}","TranslatedName of ${baseCountry.englishName}"""")
                } else {
                    languageMap[languageCode]!!.countryNameTranslations[alpha2] = translation
                }
            }

            val messageGroup = messageTranslations[languageCode]
            if (messageGroup == null || messageGroup.xmlMessages().any { it.second.contains("_TODO") }) {
                errors.add(
                    "\n\nMissing message translation(s) for ${language.name}($languageCode)" +
                            "\nAdd it to MultilingualCPMessages.csv\n\n"
                )
            } else {
                languageMap[languageCode]!!.messageGroup = messageGroup
            }
        }

        if (errors.isNotEmpty()) {
            throw MissingResourceException(errors.joinToString("\n"))
        } else {
            return languageMap.values.toList()
        }
    }

    fun getSingleLanguageTranslationsFromIP2Location(language: SupportedLanguage): Map<String, String>{
        val filePath = "$dataGeneratorRootDir/${CPFilePath.ip2LocationMultilingual}"
        return getSingleLanguageTranslationsFromFilePath(filePath, language)
    }

    fun getSingleLanguageTranslationsFromAdditionalData(language: SupportedLanguage): Map<String, String>{
        val filePath = "$dataGeneratorRootDir/${CPFilePath.translationFile(language)}"
        return getSingleLanguageTranslationsFromFilePath(filePath, language)
    }

    private fun getSingleLanguageTranslationsFromFilePath(path: String, language: SupportedLanguage): Map<String, String>{
        val reader = Files.newBufferedReader(Paths.get(path))
        val translations = mutableMapOf<String, String>()
        // parse the file into csv values
        val csvParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        for (row in csvParser) {
            if (row[LANG_CODE] == language.identifier) {
                translations[row[ALPHA_2]] = row[TRANSLATION]
            }
        }
        reader.close()
        return translations
    }

    fun getMessageTranslations(
        messageTranslationFilePath: String,
        supportedLangCodes: List<String>
    ): MutableMap<LanguageCode, MessageGroup> {
        val messageTranslations = mutableMapOf<LanguageCode, MessageGroup>()
        //read message translations
        val messageReader = Files.newBufferedReader(Paths.get(messageTranslationFilePath))
        // parse the file into csv values
        val messageCsvParser = CSVParser(
            messageReader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        for (row in messageCsvParser) {
            if (row[LANG_CODE] in supportedLangCodes) {
                messageTranslations[row[LANG_CODE]] =
                    MessageGroup(
                        row[NO_MATCH_MSG],
                        row[SEARCH_HINT],
                        row[DIALOG_TITLE],
                        row[SELECTION_PLACEHOLDER],
                        row[CLEAR_SELECTION]
                    )
            }
        }

        return messageTranslations
    }

    fun getTranslationNames(
        baseCountries: List<BaseCountry>,
        ip2locationTranslationFilePath: String,
        translationDirPath: String,
        supportedLangCodes: List<String>
    ): Map<LanguageCode, MutableMap<String, String>> {
        val nameTranslations = mutableMapOf<LanguageCode, MutableMap<String, String>>()
        // put country.english names as ENGLISH translations
        baseCountries.forEach { country ->
            nameTranslations.getOrPut(
                SupportedLanguage.ENGLISH.identifier
            ) { mutableMapOf() }[country.alpha2] = country.englishName
        }

        //read country name countryNameTranslations
        val translationFiles : List<String> =
            Files.walk(Paths.get(translationDirPath), 1, FileVisitOption.FOLLOW_LINKS)
                .filter { Files.isDirectory(it).not() }
                .map { "$translationDirPath/${it.fileName}" }.toList()
        for (filePath in listOf(ip2locationTranslationFilePath) + translationFiles) {
            val reader = Files.newBufferedReader(Paths.get(filePath))
            // parse the file into csv values
            val csvParser = CSVParser(
                reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
            )

            for (row in csvParser) {
                if (row[LANG_CODE] in supportedLangCodes) {
                    nameTranslations.getOrPut(row[LANG_CODE], { mutableMapOf() })[row[ALPHA_2]] =
                        row[TRANSLATION]
                }
            }
            reader.close()
        }
        print(nameTranslations)
        return nameTranslations
    }
}