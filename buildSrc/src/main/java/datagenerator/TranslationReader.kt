import datagenerator.SupportedLanguage
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.gradle.api.resources.MissingResourceException
import java.nio.file.Files
import java.nio.file.Paths


private const val LANG_CODE = "LANG"
private const val ALPHA_2 = "COUNTRY_ALPHA2_CODE"
private const val TRANSLATION = "COUNTRY_NAME"
private const val NO_MATCH_MSG = "NoMatchMsg"
private const val SEARCH_HINT = "SearchHint"
private const val DIALOG_TITLE = "DialogTitle"
private const val CLEAR_SELECTION = "ClearSelection"
private const val SELECTION_PHONEHOLDER = "SelectionPlaceholder"
typealias LanguageCode = String

class TranslationReader(val dataGeneratorRootDir: String) {
    fun readAllTranslations(
        baseCountries: List<BaseCountry>,
        countryTranslationFilePath: String = "$dataGeneratorRootDir/data/ip2location/IP2LOCATION-COUNTRY-MULTILINGUAL.CSV",
        additionalCountryTranslationFilePath: String = "$dataGeneratorRootDir/data/ADDITIONAL-COUNTRY-MULTILINGUAL.CSV",
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
            countryTranslationFilePath,
            additionalCountryTranslationFilePath,
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
                if (translation == null) {
                    if (isErrorHeaderAdded.not()) {
                        isErrorHeaderAdded = true
                        errors.add("\n\n\nMissing ${language.name}($languageCode) translation(s). Add following to ADDITIONAL-COUNTRY-MULTILINGUAL.CSV")
                    }
                    errors.add(""""$languageCode","${alpha2}","TranslatedName of ${baseCountry.englishName}"""")
                } else {
                    languageMap[languageCode]!!.countryNameTranslations[alpha2] = translation
                }
            }

            val messageGroup = messageTranslations[languageCode]
            if (messageGroup == null) {
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

    private fun getMessageTranslations(
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
                        row[SELECTION_PHONEHOLDER],
                        row[CLEAR_SELECTION]
                    )
            }
        }

        return messageTranslations
    }

    fun getTranslationNames(
        baseCountries: List<BaseCountry>,
        countryTranslationFilePath: String,
        additionalCountryTranslationFilePath: String,
        supportedLangCodes: List<String>
    ): Map<LanguageCode, MutableMap<String, String>> {
        val nameTranslations = mutableMapOf<LanguageCode, MutableMap<String, String>>()
        // put country.english names as ENGLISH translations
        baseCountries.forEach { country ->
            nameTranslations.getOrPut(
                SupportedLanguage.ENGLISH.identifier,
                { mutableMapOf() })[country.alpha2] = country.englishName
        }

        //read country name countryNameTranslations
        for (filePath in listOf(countryTranslationFilePath, additionalCountryTranslationFilePath)) {
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