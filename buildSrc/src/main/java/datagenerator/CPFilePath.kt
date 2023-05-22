package datagenerator

object CPFilePath {
    fun translationFile(supportedLanguage: SupportedLanguage): String {
        return "data/translations/${supportedLanguage.identifier.toUpperCase()}_translations.csv"
    }

    val phoneCodesCsv = "data/PhoneCodes.CSV"
    val flagEmojiCsv = "data/FlagEmojis.CSV"
    val ip2LocationMultilingual= "data/ip2location/IP2LOCATION-COUNTRY-MULTILINGUAL.CSV"
    val multilingualCPMessages= "data/MultilingualCPMessages.csv"
}
