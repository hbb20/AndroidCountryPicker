import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
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

class TranslationReader(val dataGeneratorRootDir: String) {
    fun readAllTranslations(
        countryTranslationFilePath: String = "$dataGeneratorRootDir/data/ip2location/IP2LOCATION-COUNTRY-MULTILINGUAL.CSV",
        messageTranslationFilePath: String = "$dataGeneratorRootDir/data/MultilingualCPMessages.CSV"
    ): List<LanguageTranslation> {
        val languageMap = mutableMapOf<String, LanguageTranslation>()
        for (supportedLanguage in SupportedLanguage.values()) {
            languageMap[supportedLanguage.identifier] =
                LanguageTranslation(supportedLanguage, mutableMapOf())
        }

        val supportedLangCodes = languageMap.keys.map { it.toUpperCase() }

        //read country name countryNameTranslations
        val reader = Files.newBufferedReader(Paths.get(countryTranslationFilePath))
        // parse the file into csv values
        val csvParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        for (row in csvParser) {
            if (row[LANG_CODE] in supportedLangCodes) {
                languageMap[row[LANG_CODE]]?.countryNameTranslations?.set(
                    row[ALPHA_2],
                    row[TRANSLATION]
                )
            }
        }

        //read message countryNameTranslations
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
                languageMap[row[LANG_CODE]]?.messageGroup =
                    MessageGroup(
                        row[NO_MATCH_MSG],
                        row[SEARCH_HINT],
                        row[DIALOG_TITLE],
                        row[SELECTION_PHONEHOLDER],
                        row[CLEAR_SELECTION]
                    )
            }
        }

        return languageMap.values.toList()
    }
}