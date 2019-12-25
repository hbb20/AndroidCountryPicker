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
        customExcludedCountries: String = "",
        countryFileReader: CountryFileReading = CountryFileReader
    ): CPDataStore {
        val xmlDataStore = countryFileReader.loadDataStoreFromXML(context, defaultLanguage)
        masterCountryList = xmlDataStore.countryList
        masterListLanguage = xmlDataStore.cpLanguage
        var countryList =
            filterCustomMasterList(xmlDataStore.countryList, customMasterCountries)
        countryList = filterExcludedCountriesList(countryList, customExcludedCountries)
        return xmlDataStore.copy(countryList = countryList)
    }

    private fun filterExcludedCountriesList(
        countryList: List<CPCountry>,
        customExcludedCountries: String
    ): List<CPCountry> {
        val countryAlphaCodes = customExcludedCountries.split(",").map { it.trim() }
        val filteredCountries = countryList.filterNot {
            countryAlphaCodes.contains(it.alpha2Code) || countryAlphaCodes.contains(it.alpha3Code)
        }
        return if (filteredCountries.isNotEmpty()) {
            filteredCountries
        } else {
            countryList
        }
    }

    private fun filterCustomMasterList(
        masterCountryList: List<CPCountry>,
        customExcludedCountries: String
    ): List<CPCountry> {
        val countryAlphaCodes = customExcludedCountries.split(",").map { it.trim() }
        val customMasterCountries = masterCountryList.filter {
            countryAlphaCodes.contains(it.alpha2Code) || countryAlphaCodes.contains(it.alpha3Code)
        }

        /**
         * If there is no valid master country, return default master list
         */
        return if (customMasterCountries.isNotEmpty()) {
            customMasterCountries
        } else {
            masterCountryList
        }
    }
}