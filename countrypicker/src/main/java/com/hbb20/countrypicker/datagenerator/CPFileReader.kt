package com.hbb20.countrypicker.datagenerator

import android.content.Context
import android.content.res.Resources
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.logger.logMethodEnd
import com.hbb20.countrypicker.logger.onMethodBegin
import com.hbb20.countrypicker.models.BaseCountry
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import masterBaseList


interface CountryFileReading {
    fun readMasterDataFromFiles(
        context: Context
    ): CPDataStore
}

object CPFileReader : CountryFileReading {
    private var baseCountries: List<BaseCountry> = masterBaseList

    override fun readMasterDataFromFiles(context: Context): CPDataStore {
        onMethodBegin("readMasterDataFromFiles")
        val messageGroup =
            loadMessageGroup(
                context.resources
            )
        val translations =
            loadCountryNameTranslations(
                context
            )
        val cpCountries = baseCountries.map { CPCountry.from(it, translations[it.alpha2]) }
        logMethodEnd("readMasterDataFromFiles")
        return CPDataStore(
            cpCountries.toMutableList(),
            messageGroup
        )
    }

    /**
     * this will load the translations
     */
    private fun loadCountryNameTranslations(context: Context): HashMap<String, String> {
        onMethodBegin("loadCountryNameTranslations")
        val result = hashMapOf<String, String>()
        masterBaseList.forEach { country ->
            val resId: Int = context.resources.getIdentifier(
                "cp_${country.alpha2}_name",
                "string",
                context.packageName
            )
            val name: String = context.getString(resId)
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
        val messageGroup = CPDataStore.MessageGroup(
            noMatchMsg = resources.getString(R.string.cp_no_match_msg),
            searchHint = resources.getString(R.string.cp_search_hint),
            dialogTitle = resources.getString(R.string.cp_dialog_title),
            selectionPlaceholderText = resources.getString(R.string.cp_selection_placeholder),
            clearSelectionText = resources.getString(R.string.cp_clear_selection)
        )
        logMethodEnd("loadMessageGroup")
        return messageGroup
    }
}
