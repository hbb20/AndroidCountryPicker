import datagenerator.SupportedLanguage

class CPDataExtractor(val projectDir: String) {
    val dataGeneratorRootPath = "$projectDir/buildSrc/src/main/java/datagenerator"
    fun generate(): Unit {
        val ip2LocationInfoMap = IP2LocationInfoReader(dataGeneratorRootPath).read()
        val additionalCountryInfoMap = AdditionalCountryInfoReader(dataGeneratorRootPath).read()
        val phoneCodeMap = PhoneCodeReader(dataGeneratorRootPath).read()
        val flagEmojiMap = EmojiReader(dataGeneratorRootPath).read()
        val baseCountries = prepareBaseCountryList(
            ip2LocationInfoMap,
            additionalCountryInfoMap,
            phoneCodeMap,
            flagEmojiMap
        )
        //    FileWriter.writeBaseCountries(baseCountries = baseCountries.values.toList())
        val fileWriter = FileWriter(projectDir)
        fileWriter.writeBaseCountriesKt(baseCountries = baseCountries.values.toList())

        //countryNameTranslations$
        val languageTranslations =
            TranslationReader(dataGeneratorRootPath).readAllTranslations(baseCountries.values.toList())
        languageTranslations.forEach {
            fileWriter.writeLanguageTranslation(languageTranslation = it)
            if (it.language == SupportedLanguage.ENGLISH) {
                fileWriter.writeDefaultTranslationFile(languageTranslation = it)
            }
        }
    }
}

fun prepareBaseCountryList(
    ip2LocationInfoMap: MutableMap<String, IP2LocationInfoCountry>,
    additionalCountryInfoMap: MutableMap<String, AdditionalCountryInfo>,
    phoneCodeMap: Map<String, Int>,
    flagEmojiMap: Map<String, String>
): Map<String, BaseCountry> {
    val result = mutableMapOf<String, BaseCountry>()
    ip2LocationInfoMap.forEach { (alpha2, info) ->
        val phoneCode = phoneCodeMap[alpha2]
        val flagEmoji = flagEmojiMap[alpha2]
        if (phoneCode == null) {
            throw Exception("No phone code found for $alpha2 (${info.englishName}). Add in PhoneCodes.csv from https://countrycode.org/")
        }
        if (flagEmoji == null) {
            printErr("No flag emoji found for $alpha2 (${info.englishName})")
        }
        result[alpha2] = BaseCountry(info, flagEmoji ?: " ", phoneCode)
    }

    additionalCountryInfoMap.forEach { (alpha2, info) ->
        val phoneCode = phoneCodeMap[alpha2]
        val flagEmoji = flagEmojiMap[alpha2]
        if (phoneCode == null) {
            throw Exception("No phone code found for $alpha2 (${info.englishName}). Add in PhoneCodes.csv")
        }
        if (flagEmoji == null) {
            throw Exception(
                "No emoji flag found for $alpha2 (${info.englishName}). " +
                        "Add in FlagEmojis.csv from https://emojipedia.org/flags/. " +
                        "If there is not emoji available, add empty space."
            )
        }
        result[alpha2] = BaseCountry(info, flagEmoji, phoneCode)
    }
    return result.toSortedMap()
}

fun printErr(errorMsg: String) {
    System.err.println(errorMsg)
}