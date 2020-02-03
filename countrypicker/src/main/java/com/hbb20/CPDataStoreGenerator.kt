package com.hbb20

import android.content.Context

object CPDataStoreGenerator {
    private var masterDataStore: CPDataStore? = null

    fun generate(
        context: Context,
        customMasterCountries: String = "",
        customExcludedCountries: String = "",
        countryFileReader: CountryFileReading = DefaultCountryFileReader
    ): CPDataStore {

        if (masterDataStore == null) {
            masterDataStore = countryFileReader.readDataStoreFromFile(context)
        }

        masterDataStore?.let {
            var countryList =
                filterCustomMasterList(it.countryList, customMasterCountries)
            countryList = filterExcludedCountriesList(countryList, customExcludedCountries)
            return it.copy(countryList = countryList.toMutableList())
        }

        throw IllegalStateException("MasterDataStore can not be null at this point.")
    }

    private fun filterExcludedCountriesList(
        countryList: List<CPCountry>,
        customExcludedCountries: String
    ): List<CPCountry> {
        val countryAlphaCodes = customExcludedCountries.split(",").map { it.trim() }
        val filteredCountries = countryList.filterNot {
            countryAlphaCodes.contains(it.alpha2) || countryAlphaCodes.contains(it.alpha3)
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
            countryAlphaCodes.contains(it.alpha2) || countryAlphaCodes.contains(it.alpha3)
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