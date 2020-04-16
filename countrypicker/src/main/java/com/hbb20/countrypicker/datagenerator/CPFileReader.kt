package com.hbb20.countrypicker.datagenerator

import android.content.res.Resources
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.logger.logMethodEnd
import com.hbb20.countrypicker.logger.onMethodBegin
import com.hbb20.countrypicker.models.BaseCountry
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.InputStream


interface CountryFileReading {
    fun readMasterDataFromFiles(
        resources: Resources
    ): CPDataStore
}

object CPFileReader : CountryFileReading {
    private lateinit var baseCountries: List<BaseCountry>

    override fun readMasterDataFromFiles(resources: Resources): CPDataStore {
        onMethodBegin("readMasterDataFromFiles")
        loadBaseListFromCsv(
            resources
        )
        val messageGroup =
            loadMessageGroup(
                resources
            )
        val translations =
            loadCountryNameTranslationsFromCsv(
                resources
            )
        val cpCountries = baseCountries.map { CPCountry.from(it, translations[it.alpha2]) }
        logMethodEnd("readMasterDataFromFiles")
        return CPDataStore(
            cpCountries.toMutableList(),
            messageGroup
        )
    }

    /**
     * this will load the base list only if it's not already initialized.
     */
    private fun loadBaseListFromCsv(resources: Resources) {
        onMethodBegin("loadBaseListFromCsv")
        val ins: InputStream = resources.openRawResource(R.raw.cp_country_info)

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
        CPFileReader.baseCountries = baseCountries
        ins.close()
        logMethodEnd("loadBaseListFromCsv")
    }

    /**
     * this will load the translations
     */
    private fun loadCountryNameTranslationsFromCsv(resources: Resources): HashMap<String, String> {
        onMethodBegin("loadCountryNameTranslationsFromCsv")
        val ins: InputStream = resources.openRawResource(R.raw.cp_country_translation)
        // parse the file into csv values
        val csvParser = CSVParser(
            ins.bufferedReader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )

        val translations = hashMapOf<String, String>()
        for (row in csvParser) {
            translations[row["alpha2"]] = row["translatedName"]
        }

        ins.close()
        logMethodEnd("loadCountryNameTranslationsFromCsv")
        return translations
    }

    /**
     * this will load the messageCollection from csv
     */
    private fun loadMessageGroup(resources: Resources): CPDataStore.MessageGroup {
        onMethodBegin("loadMessageGroup")
        val messageGroup = CPDataStore.MessageGroup(
            noMatchMsg = resources.getString(R.string.cp_no_match_msg),
            searchHint = resources.getString(R.string.cp_search_hint),
            dialogTitle = resources.getString(R.string.cp_dialog_title),
            selectionPlaceholderText = resources.getString(R.string.cp_selection_place_holder),
            clearSelectionText = resources.getString(R.string.cp_clear_selection)
        )
        logMethodEnd("loadMessageGroup")
        return messageGroup
    }
}
