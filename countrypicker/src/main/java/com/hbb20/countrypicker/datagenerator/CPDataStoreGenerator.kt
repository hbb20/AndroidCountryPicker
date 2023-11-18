package com.hbb20.countrypicker.datagenerator

import android.content.Context
import com.hbb20.countrypicker.logger.onMethodBegin
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import java.util.*

object CPDataStoreGenerator {
    private var masterDataStore: CPDataStore? = null
    const val defaultMasterCountries = ""
    const val defaultExcludedCountries = ""
    @Deprecated("no-longer-used")
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
            return it.copy(
                countryList = countryList.sortedBy { cpCountry -> cpCountry.name }
                    .toMutableList()
            )
        }

        throw IllegalStateException("MasterDataStore can not be null at this point.")
    }

    fun generate(
        context: Context,
        countryFileReader: CountryFileReading = defaultCountryFileReader,
        countryListTransformer: ((List<CPCountry>) -> List<CPCountry>)?
    ): CPDataStore {
        onMethodBegin("GenerateDataStore")
        if (masterDataStore == null) {
            masterDataStore = countryFileReader.readMasterDataFromFiles(context)
        }

        masterDataStore?.let {
            var countryList: List<CPCountry> = it.countryList
            countryList = countryList.sortedBy { cpCountry -> cpCountry.name }
            countryListTransformer?.let { transformer ->
                countryList = transformer(countryList)
            }
            return it.copy(countryList.toMutableList())
        }

        throw IllegalStateException("MasterDataStore can not be null at this point.")
    }

    private fun filterExcludedCountriesList(
        countryList: List<CPCountry>,
        customExcludedCountries: String
    ): List<CPCountry> {
        val countryAlphaCodes =
            customExcludedCountries.split(",").map { it.trim().toUpperCase(Locale.US) }
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
        val countryAlphaCodes =
            customExcludedCountries.split(",").map { it.trim().toUpperCase(Locale.US) }
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
