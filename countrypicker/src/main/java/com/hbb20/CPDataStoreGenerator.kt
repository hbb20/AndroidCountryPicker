package com.hbb20

import android.content.Context

object CPDataStoreGenerator {
    private var masterCountryList: List<CPCountry>? = null
    private var masterListLanguage: CPLanguage? = null

    fun generate(
        context: Context,
        defaultLanguage: CPLanguage = CPLanguage.ENGLISH,
        autoDetectLanguage: Boolean = false,
        customMasterCountries: String = "",
        customExcludedCountries: String = ""
    ): CPDataStore {
        val xmlDataStore = CountryFileReader.loadDataStoreFromXML(context, defaultLanguage)
        masterCountryList = xmlDataStore.masterCountryList
        masterListLanguage = xmlDataStore.cpLanguage
        return xmlDataStore
    }
}