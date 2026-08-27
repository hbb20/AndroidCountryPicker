package com.hbb20.countrypicker.datagenerator

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.logger.logMethodEnd
import com.hbb20.countrypicker.logger.onMethodBegin
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import com.hbb20.countrypicker.models.CountryInfo
import com.hbb20.countrypicker.models.countryInfoList

interface CountryFileReading {
    fun readMasterDataFromFiles(context: Context): CPDataStore
}

object CPFileReader : CountryFileReading {
    private var countryInfos: List<CountryInfo> = countryInfoList

    override fun readMasterDataFromFiles(context: Context): CPDataStore {
        onMethodBegin("readMasterDataFromFiles")
        val messageGroup =
            loadMessageGroup(
                context.resources,
            )
        val translations =
            loadCountryNameTranslations(
                context,
            )
        val cpCountries = countryInfos.map { CPCountry.from(it, translations[it.alpha2]) }
        logMethodEnd("readMasterDataFromFiles")
        return CPDataStore(
            cpCountries.toMutableList(),
            messageGroup,
        )
    }

    /**
     * this will load the translations
     */
    private fun loadCountryNameTranslations(context: Context): HashMap<String, String> {
        onMethodBegin("loadCountryNameTranslations")
        val result = hashMapOf<String, String>()
        countryInfoList.forEach { country ->
            Log.d("CountryPicker", "Lookinng up resourcee for cp_${country.alpha2}_name")
            val resId: Int =
                context.resources.getIdentifier(
                    "cp_${country.alpha2}_name",
                    "string",
                    context.packageName,
                )

            val name: String = context.getString(resId)
            Log.d("CountryPicker", "found resourcee for cp_${country.alpha2}_name = $name")
            result[country.alpha2] = name
        }
        logMethodEnd("loadCountryNameTranslations")
        return result
    }

    /**
     * this will load the messageCollection from csv
     */
    private fun loadMessageGroup(resources: Resources): CPDataStore.MessageGroup {
        onMethodBegin("loadMessageGroup")
        val messageGroup =
            CPDataStore.MessageGroup(
                noMatchMsg = resources.getString(R.string.cp_no_match_msg),
                searchHint = resources.getString(R.string.cp_search_hint),
                dialogTitle = resources.getString(R.string.cp_dialog_title),
                selectionPlaceholderText = resources.getString(R.string.cp_selection_placeholder),
                clearSelectionText = resources.getString(R.string.cp_clear_selection),
            )
        logMethodEnd("loadMessageGroup")
        return messageGroup
    }
}
