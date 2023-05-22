import datagenerator.SupportedLanguage
import datagenerator.CPFilePath
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

private const val NO_MATCH_MSG = "NoMatchMsg"
private const val SEARCH_HINT = "SearchHint"
private const val DIALOG_TITLE = "DialogTitle"
private const val SELECTION_PHONEHOLDER = "SelectionPlaceholder"

class FileWriter(val projectRootDir: String) {
    val resDir = "${projectRootDir}/countrypicker/src/main/res"
    val cpModelDir = "${projectRootDir}/countrypicker/src/main/java/com/hbb20/countrypicker/models"
    fun writeBaseCountries(
        resDirPath: String = resDir,
        baseCountries: List<BaseCountry>
    ) {
        val filePath = "$resDirPath/raw/cp_country_info.csv"
        val file = File(filePath)
        file.parentFile.mkdirs()

        val writer = Files.newBufferedWriter(Paths.get(filePath))
        val csvPrinter = CSVPrinter(
            writer, CSVFormat.DEFAULT
                .withHeader(
                    "alpha2",
                    "alpha3",
                    "englishName",
                    "demonym",
                    "numericCode",
                    "capitalEnglishName",
                    "areaKM2",
                    "population",
                    "currencyCode",
                    "currencyName",
                    "currencySymbol",
                    "cctld",
                    "flagEmoji",
                    "phoneCode"
                )
        )

        for (baseCountry in baseCountries) {
            baseCountry.apply {
                csvPrinter.printRecord(
                    alpha2,
                    alpha3,
                    englishName,
                    demonym,
                    capitalEnglishName,
                    areaKM2,
                    population,
                    currencyCode,
                    currencyName,
                    currencySymbol,
                    cctld,
                    flagEmoji,
                    phoneCode
                )
            }
        }
        csvPrinter.flush()
        csvPrinter.close()
    }

    fun writeBaseCountriesKt(
        cpModelDirPath: String = cpModelDir,
        baseCountries: List<BaseCountry>
    ) {
        val filePath = "$cpModelDirPath/MasterInfoList.kt"
        val file = File(filePath)
        file.parentFile.mkdirs()

        val writer = Files.newBufferedWriter(Paths.get(filePath))
        writer.apply {
            appendLine(
                "package com.hbb20.countrypicker.models\n" +
                        "\n" +
                        "internal val countryInfoList = listOf(\n"
            )
            baseCountries.forEachIndexed { index, it ->
                var line =
                    "    CountryInfo(\n" +
                            "        alpha2 = \"${it.alpha2}\",\n" +
                            "        alpha3 = \"${it.alpha3}\",\n" +
                            "        englishName = \"${it.englishName}\",\n" +
                            "        demonym = \"${it.demonym}\",\n" +
                            "        capitalEnglishName = \"${it.capitalEnglishName}\",\n" +
                            "        areaKM2 = \"${it.areaKM2}\",\n" +
                            "        population = ${it.population},\n" +
                            "        currencyCode = \"${it.currencyCode}\",\n" +
                            "        currencyName = \"${it.currencyName}\",\n" +
                            "        currencySymbol = \"${it.currencySymbol}\",\n" +
                            "        cctld = \"${it.cctld}\",\n" +
                            "        flagEmoji = \"${it.flagEmoji}\",\n" +
                            "        phoneCode = ${it.phoneCode}\n" +
                            "    )"
                if (index != baseCountries.lastIndex) line += ","
                appendLine(line)
            }

            appendLine(")")
        }
        writer.flush()
        writer.close()

    }

    fun writeLanguageTranslation(
        resDirPath: String = resDir,
        languageTranslation: LanguageTranslation
    ) {
        val countryNameTranslationFilePath =
            resDirPath + "/values-${getLocalDirIdentifier(languageTranslation.language)}/cp_country_translation.xml"
        val messageTranslationFilePath =
            resDirPath + "/values-${getLocalDirIdentifier(languageTranslation.language)}/cp_message_translation.xml"
        writeTranslation(
            countryNameTranslationFilePath,
            messageTranslationFilePath,
            languageTranslation
        )
    }

    private fun getLocalDirIdentifier(language: SupportedLanguage): String {
        return if (language.identifier.contains("-")) {
            val parts = language.identifier.split("-")
            return parts[0].toLowerCase() + "-r" + parts[1].toUpperCase()
        } else {
            language.identifier.toLowerCase()
        }
    }

    fun writeDefaultTranslationFile(
        resDirPath: String = resDir,
        languageTranslation: LanguageTranslation
    ) {
        val countryNameTranslationFilePath = "$resDirPath/values/cp_country_translation.xml"
        val messageTranslationFilePath = "$resDirPath/values/cp_message_translation.xml"
        writeTranslation(
            countryNameTranslationFilePath,
            messageTranslationFilePath,
            languageTranslation
        )
    }

    private fun writeTranslation(
        countryNameTranslationFilePath: String,
        messageTranslationFilePath: String,
        languageTranslation: LanguageTranslation
    ) {
        val countryTranslationFile = File(countryNameTranslationFilePath)
        val messageTranslationFile = File(messageTranslationFilePath)
        countryTranslationFile.parentFile.mkdirs()
        messageTranslationFile.parentFile.mkdirs()

        //country name
        val countryNamewriter =
            Files.newBufferedWriter(Paths.get(countryNameTranslationFilePath))
        countryNamewriter.apply {
            appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            appendLine("<resources>")
            languageTranslation.countryNameTranslations.forEach { (alpha2, name) ->
                appendLine(
                    "    <string name=\"cp_${alpha2}_name\">${
                        name.replace(
                            "'",
                            "\\'"
                        )
                    }</string>"
                )
            }
            appendLine("</resources>")
        }
        countryNamewriter.flush()
        countryNamewriter.close()

        //messages
        val messagesWriter = Files.newBufferedWriter(Paths.get(messageTranslationFilePath))
        messagesWriter.apply {
            appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            appendLine("<resources>")
            languageTranslation.messageGroup?.xmlMessages()?.forEach {
                appendLine("    <string name=\"${it.first}\">${it.second}</string>")
            }
            appendLine("</resources>")
        }
        messagesWriter.flush()
        messagesWriter.close()
    }
}