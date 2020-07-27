package com.hbb20.countrypicker.datagenerator

import android.content.Context
import com.hbb20.countrypicker.logger.onMethodBegin
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore

object CPDataStoreGenerator {
    private var masterDataStore: CPDataStore? = null
    const val defaultMasterCountries = ""
    const val defaultExcludedCountries = ""
    const val defaultUseCache = true
    val defaultCountryFileReader = CPFileReader

    fun generate(
        context: Context,
        customMasterCountries: String = defaultMasterCountries,
        customExcludedCountries: String = defaultExcludedCountries,
        countryFileReader: CountryFileReading = defaultCountryFileReader,
        useCache: Boolean = defaultUseCache
    ): CPDataStore {
        onMethodBegin("GenerateDataStore")
        if (masterDataStore == null || !useCache) {
            masterDataStore = countryFileReader.readMasterDataFromFiles(context)
        }

        masterDataStore?.let {
            var countryList =
                filterCustomMasterList(
                    it.countryList,
                    customMasterCountries
                )
            countryList =
                filterExcludedCountriesList(
                    countryList,
                    customExcludedCountries
                )
            return it.copy(countryList = countryList.sortedBy { cpCountry -> cpCountry.name }
                .toMutableList())
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

    fun invalidateCache() {
        masterDataStore = null
    }
}