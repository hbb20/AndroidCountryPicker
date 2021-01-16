fun main(args: Array<String>) {
    val infoMap = InfoReader().read()
    val phoneCodeMap = PhoneCodeReader().read()
    val flagEmojiMap = EmojiReader().read()
    val baseCountries = prepareBaseCountryList(infoMap, phoneCodeMap, flagEmojiMap)
    //    FileWriter.writeBaseCountries(baseCountries = baseCountries.values.toList())
    FileWriter.writeBaseCountriesKt(baseCountries = baseCountries.values.toList())

    //countryNameTranslations$
    val languageTranslations = TranslationReader.readAllTranslations()
    languageTranslations.forEach {
        FileWriter.writeLanguageTranslation(languageTranslation = it)
        if (it.language == SupportedLanguage.ENGLISH) {
            FileWriter.writeDefaultTranslationFile(languageTranslation = it)
        }
    }


}

fun prepareBaseCountryList(
    infoMap: MutableMap<String, InfoCountry>,
    phoneCodeMap: Map<String, Int>,
    flagEmojiMap: Map<String, String>
): Map<String, BaseCountry> {
    val result = mutableMapOf<String, BaseCountry>()
    infoMap.forEach { (alpha2, info) ->
        val phoneCode = phoneCodeMap[alpha2]
        val flagEmoji = flagEmojiMap[alpha2]
        if (phoneCode == null) {
            printErr("No phone code found for $alpha2 (${info.englishName})")
        }
        if (flagEmoji == null) {
            printErr("No flag emoji found for $alpha2 (${info.englishName})")
        }
        result[alpha2] = BaseCountry(info, flagEmoji ?: " ", phoneCode)
    }
    return result
}

fun printErr(errorMsg: String) {
    System.err.println(errorMsg)
}