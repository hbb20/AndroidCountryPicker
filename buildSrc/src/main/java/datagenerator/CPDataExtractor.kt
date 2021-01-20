class CPDataExtractor(val projectDir: String) {
    val dataGeneratorRootPath = "$projectDir/buildSrc/src/main/java/datagenerator"
    fun generate(): Unit {
        print("root dir is $projectDir/buildSrc/")
        val infoMap = InfoReader(dataGeneratorRootPath).read()
        val phoneCodeMap = PhoneCodeReader(dataGeneratorRootPath).read()
        val flagEmojiMap = EmojiReader(dataGeneratorRootPath).read()
        val baseCountries = prepareBaseCountryList(infoMap, phoneCodeMap, flagEmojiMap)
        //    FileWriter.writeBaseCountries(baseCountries = baseCountries.values.toList())
        val fileWriter = FileWriter(projectDir)
        fileWriter.writeBaseCountriesKt(baseCountries = baseCountries.values.toList())

        //countryNameTranslations$
        val languageTranslations = TranslationReader(dataGeneratorRootPath).readAllTranslations()
        languageTranslations.forEach {
            fileWriter.writeLanguageTranslation(languageTranslation = it)
            if (it.language == SupportedLanguage.ENGLISH) {
                fileWriter.writeDefaultTranslationFile(languageTranslation = it)
            }
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