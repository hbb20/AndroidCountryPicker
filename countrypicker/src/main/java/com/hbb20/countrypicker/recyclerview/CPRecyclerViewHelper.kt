package com.hbb20.countrypicker.recyclerview

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.countrypicker.config.CPListConfig
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore

class CPRecyclerViewHelper(
    private val cpDataStore: CPDataStore,
    cpListConfig: CPListConfig = CPListConfig(),
    private val cpRowConfig: CPRowConfig = CPRowConfig(),
    onCountryClickListener: ((CPCountry) -> Unit)
) {

    var allPreferredCountries = extractPreferredCountries(
        cpDataStore.countryList,
        cpListConfig.preferredCountryCodes
    )
        private set

    val epoxyController by lazy { CountryListController() }

    val controllerData = CountryListControllerData(
        allPreferredCountries,
        cpDataStore.countryList,
        onCountryClickListener,
        cpRowConfig,
        cpDataStore
    )

    fun attachFilterQueryEditText(queryEditText: EditText?) {
        queryEditText?.doOnTextChanged { query, _, _, _ ->
            updateViewForQuery(query.toString())
        }
    }

    private fun updateViewForQuery(query: String) {
        updateDataForQuery(query)
        epoxyController.setData(controllerData)
    }

    fun updateDataForQuery(query: String) {
        controllerData.preferredCountries =
            allPreferredCountries.filterCountries(query, cpRowConfig)
        controllerData.allCountries =
            cpDataStore.countryList.filterCountries(query, cpRowConfig)
    }

    fun attachRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        epoxyController.setData(controllerData)
        recyclerView.adapter = epoxyController.adapter
    }

    private fun extractPreferredCountries(
        countries: List<CPCountry>,
        preferredCountryCodes: String? = ""
    ): List<CPCountry> {
        val result = mutableListOf<CPCountry>()

        preferredCountryCodes?.split(",")?.map { it.trim() }?.mapNotNull { alphaCode ->
            val country = when (alphaCode.length) {
                2 -> countries.find { cpCountry -> cpCountry.alpha2.equals(alphaCode, true) }
                3 -> countries.find { cpCountry -> cpCountry.alpha3.equals(alphaCode, true) }
                else -> null
            }
            country?.let { result.add(it) }
        }

        return result.distinctBy { it.alpha2 }
    }

    private fun List<CPCountry>.filterCountries(
        filterQuery: String,
        cpRowConfig: CPRowConfig
    ): List<CPCountry> {
        if (filterQuery.isBlank()) return this

        return this.filter {
            val firstCharsOfWords =
                cpRowConfig.primaryTextGenerator(it).split(" ").filter { it.isNotBlank() }
                    .map { it[0] }.filter { it.isUpperCase() }.joinToString("")
            it.alpha2.startsWith(filterQuery, true) ||
                    it.alpha3.startsWith(filterQuery, true) ||
                    firstCharsOfWords.contains(filterQuery, true) ||
                    cpRowConfig.primaryTextGenerator(it).contains(filterQuery, true) ||
                    cpRowConfig.secondaryTextGenerator?.invoke(it)?.contains(filterQuery, true)
                    ?: false ||
                    cpRowConfig.highlightedTextGenerator?.invoke(it)?.contains(filterQuery, true)
                    ?: false

        }
    }
}
