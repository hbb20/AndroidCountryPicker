package com.hbb20

import android.content.Context
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.InputStream


interface CountryFileReading {
    fun readDataStoreFromFile(
        context: Context
    ): CPDataStore
}

object DefaultCountryFileReader : CountryFileReading {
    private lateinit var baseCountries: List<BaseCountry>

    override fun readDataStoreFromFile(context: Context): CPDataStore {
        loadBaseListFromCsv(context)
        val messageCollection = loadMessageCollectionFromCsv(context)
        val translations = loadCountryNameTranslationsFromCsv(context)
        val cpCountries = baseCountries.map { CPCountry.from(it, translations[it.alpha2]) }
        return CPDataStore(cpCountries.toMutableList(), messageCollection)
    }

    /**
     * this will load the base list only if it's not already initialized.
     */
    private fun loadBaseListFromCsv(context: Context) {
        val ins: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier(
                CP_COUNTRY_INFO_CSV,
                "raw", context.packageName
            )
        )
        // parse the file into csv values
        val csvParser = CSVParser(
            ins.bufferedReader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        val baseCountries = mutableListOf<BaseCountry>()
        for (row in csvParser) {
            baseCountries.add(
                BaseCountry(
                    alpha2 = row["alpha2"],
                    alpha3 = row["alpha3"],
                    englishName = row["englishName"],
                    demonym = row["demonym"],
                    capitalEnglishName = row["capitalEnglishName"],
                    areaKM2 = row["areaKM2"],
                    population = row["population"].toLong(),
                    currencyCode = row["currencyCode"],
                    currencyName = row["currencyName"],
                    currencySymbol = row["currencySymbol"],
                    cctld = row["cctld"],
                    flagEmoji = row["flagEmoji"],
                    phoneCode = row["phoneCode"].toShort()
                )
            )
        }
        this.baseCountries = baseCountries
        ins.close()
    }

    /**
     * this will load the translations
     */
    private fun loadCountryNameTranslationsFromCsv(context: Context): MutableMap<String, String> {
        val ins: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier(
                CP_COUNTRY_TRANSLATION_CSV,
                "raw", context.packageName
            )
        )
        // parse the file into csv values
        val csvParser = CSVParser(
            ins.bufferedReader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        val translations = mutableMapOf<String, String>()
        for (row in csvParser) {
            translations[row["alpha2"]] = row["translatedName"]
        }

        ins.close()
        return translations
    }

    /**
     * this will load the messageCollection from csv
     */
    private fun loadMessageCollectionFromCsv(context: Context): CPDataStore.MessageCollection {
        val ins: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier(
                CP_MESSAGE_TRANSLATION_CSV,
                "raw", context.packageName
            )
        )
        // parse the file into csv values
        val csvParser = CSVParser(
            ins.bufferedReader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        var messageCollection: CPDataStore.MessageCollection = CPDataStore.MessageCollection()
        csvParser.records[0]?.apply {
            messageCollection = CPDataStore.MessageCollection(
                this["NoMatchMsg"],
                this["SearchHint"],
                this["DialogTitle"],
                this["SelectionPlaceholder"]
            )
        }
        ins.close()
        return messageCollection
    }
}
