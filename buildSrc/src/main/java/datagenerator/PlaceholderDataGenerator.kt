import datagenerator.CPFilePath
import datagenerator.SupportedLanguage
import java.nio.file.Files
import java.nio.file.Paths

/**
 * This is to look at supported languages and add placeholder data so that
 * community can easily add language support.
 * In future, this should be updated to account for new country resource as well.
 */
class CPPlaceHolderDataGenerator(val projectDir: String) {
    val dataGeneratorRootPath = "$projectDir/buildSrc/src/main/java/datagenerator"
    fun generate(): Unit {
        print("root dir is $projectDir/buildSrc/")
        val ip2LocationInfoMap = IP2LocationInfoReader(dataGeneratorRootPath).read()
        val additionalCountryInfoMap = AdditionalCountryInfoReader(dataGeneratorRootPath).read()
        val supportedLanguages = SupportedLanguage.values()
        val alpha2toNames = ip2LocationInfoMap.mapValues { it.value.englishName }.toMutableMap()
        alpha2toNames.putAll(additionalCountryInfoMap.mapValues { it.value.englishName })


        val fileWriter = FileWriter(projectDir)
        // find missing phone codes and add placeholder
        addMissingPhoneCodePlaceholder(alpha2toNames.keys)

        // find missing emoji and add placeholder'
        addMissingEmojiPlaceholder(alpha2toNames.keys)

        // iterate over all supported languages
        addCountryNameTranslationPlaceholder(supportedLanguages, alpha2toNames)
        addCPMessageTranslationPlaceholder(supportedLanguages)
    }

    private fun addCountryNameTranslationPlaceholder(
        supportedLanguages: Array<SupportedLanguage>,
        alpha2toNames: MutableMap<String, String>
    ) {
        val translationReader = TranslationReader(dataGeneratorRootPath)
        supportedLanguages.filterNot { it == SupportedLanguage.ENGLISH }
            .forEach { supportedLanguage ->
                // create translation file if missing
                val translationFilePath =
                    Paths.get("$dataGeneratorRootPath/${CPFilePath.translationFile(supportedLanguage)}")
                if (Files.notExists(translationFilePath)) {
                    Files.createFile(translationFilePath)
                    val writer = Files.newBufferedWriter(translationFilePath)
                    writer.apply {
                        appendLine(
                            listOf(
                                TranslationReader.Companion.Header.LANG_CODE,
                                TranslationReader.Companion.Header.ALPHA_2,
                                TranslationReader.Companion.Header.ENGLISH_NAME,
                                TranslationReader.Companion.Header.TRANSLATION,
                            ).joinToString(",")
                        )
                    }
                    writer.flush()
                    writer.close()
                }

                // read content and add header if missing
                val ip2Translations =
                    translationReader.getSingleLanguageTranslationsFromIP2Location(supportedLanguage)
                val customTranslations =
                    translationReader.getSingleLanguageTranslationsFromAdditionalData(
                        supportedLanguage
                    )
                customTranslations.forEach { t, u -> println("$t -> $u") }

                // iterate over all countries and add entry if translation is not found
                val countriesMissingTranslation = alpha2toNames.map { it.key }.filterNot { alpha2 ->
                    ip2Translations.containsKey(alpha2) || customTranslations.containsKey(alpha2)
                }
                //            val countriesMissingTranslation = listOf("UP")
                // check message translation and add placeholder if missing
                if (countriesMissingTranslation.isNotEmpty()) {
                    val writer = Files.newBufferedWriter(translationFilePath)
                    writer.apply {
                        appendLine(
                            listOf(
                                TranslationReader.Companion.Header.LANG_CODE,
                                TranslationReader.Companion.Header.ALPHA_2,
                                TranslationReader.Companion.Header.ENGLISH_NAME,
                                TranslationReader.Companion.Header.TRANSLATION,
                            ).joinToString(",") { "\"$it\"" }
                        )
                        val languageCode = supportedLanguage.identifier
                        (customTranslations.keys + countriesMissingTranslation).distinct().sorted()
                            .forEach { alpha2 ->
                                val value =
                                    customTranslations[alpha2] ?: generateToDo("TRANSLATION")
                                appendLine(
                                    listOf(
                                        languageCode,
                                        alpha2,
                                        alpha2toNames[alpha2],
                                        value
                                    ).joinToString(",") { "\"$it\"" })
                            }
                        writer.flush()
                        writer.close()
                    }
                }
            }
    }

    private fun addCPMessageTranslationPlaceholder(
        supportedLanguages: Array<SupportedLanguage>,
    ) {
        val translationReader = TranslationReader(dataGeneratorRootPath)
        val existingMessageTranslations = translationReader.getMessageTranslations(
            "$dataGeneratorRootPath/${CPFilePath.multilingualCPMessages}",
            supportedLanguages.map { it.identifier })

        val translationFilePath =
            Paths.get("$dataGeneratorRootPath/${CPFilePath.multilingualCPMessages}")
        val writer = Files.newBufferedWriter(translationFilePath)
        writer.appendLine(
            listOf(
                TranslationReader.Companion.Header.LANG_CODE,
                TranslationReader.Companion.Header.NO_MATCH_MSG,
                TranslationReader.Companion.Header.SEARCH_HINT,
                TranslationReader.Companion.Header.DIALOG_TITLE,
                TranslationReader.Companion.Header.CLEAR_SELECTION,
                TranslationReader.Companion.Header.SELECTION_PLACEHOLDER,
            ).joinToString(",") { "\"$it\"" }
        )
        supportedLanguages.forEach {
            val translation = existingMessageTranslations[it.identifier]
            writer.appendLine(
                listOf(
                    it.identifier,
                    translation?.noMatchMsg ?: generateToDo("NO MATCH MESSAGE"),
                    translation?.searchHint ?: generateToDo("SEARCH HINT"),
                    translation?.noMatchMsg ?: generateToDo("NO MATCH MESSAGE"),
                    translation?.dialogTitle ?: generateToDo("DIALOG TITLE"),
                    translation?.clearSelection ?: generateToDo("CLEAR SELECTION"),
                    translation?.selectionPlaceHolder ?: generateToDo("SELECTION PLACEHOLDER")
                ).joinToString(",") { "\"$it\"" }
            )
        }
        writer.flush()
        writer.close()
    }

    fun addMissingPhoneCodePlaceholder(allAlpha2Codes: MutableSet<String>) {
        val path = "$dataGeneratorRootPath/${CPFilePath.phoneCodesCsv}"
        val existingPhoneCodes = PhoneCodeReader(dataGeneratorRootPath).read()
        val writer = Files.newBufferedWriter(Paths.get(path))
        writer.apply {
            appendLine(
                listOf(
                    PhoneCodeReader.Companion.Header.ALPHA_2,
                    PhoneCodeReader.Companion.Header.PHONE_CODE
                ).joinToString(",")
            )

            (existingPhoneCodes.keys + allAlpha2Codes).distinct().sorted().forEach { alpha2 ->
                val value = existingPhoneCodes[alpha2] ?: generateToDo("PHONE CODE")
                appendLine(listOf(alpha2, value).joinToString(","))
            }
        }
        writer.flush()
        writer.close()
    }

    fun addMissingEmojiPlaceholder(allAlpha2Codes: MutableSet<String>) {
        val path = "$dataGeneratorRootPath/${CPFilePath.flagEmojiCsv}"
        val existingEmojiFlags = EmojiReader(dataGeneratorRootPath).read()
        val writer = Files.newBufferedWriter(Paths.get(path))
        writer.apply {
            appendLine(
                listOf(
                    EmojiReader.Companion.Header.ALPHA_2,
                    EmojiReader.Companion.Header.FLAG_EMOJI
                ).joinToString(",")
            )

            (existingEmojiFlags.keys + allAlpha2Codes).distinct().sorted().forEach { alpha2 ->
                val value = existingEmojiFlags[alpha2] ?: generateToDo("EMOJI FLAG")
                appendLine(listOf(alpha2, value).joinToString(","))
            }
        }
        writer.flush()
        writer.close()
    }
}
