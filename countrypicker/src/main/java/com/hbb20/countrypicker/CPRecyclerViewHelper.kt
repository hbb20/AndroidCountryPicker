package com.hbb20.countrypicker

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CPCountry
import com.hbb20.CPDataStore
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.recyclerview.CountryListController
import com.hbb20.countrypicker.recyclerview.CountryListControllerData

class CPRecyclerViewHelper(
    private val cpDataStore: CPDataStore,
    onCountryClickListener: ((CPCountry) -> Unit),
    private val cpCountryRowConfig: CPCountryRowConfig = CPCountryRowConfig(),
    preferredCountryCodes: String? = null,
    preferredCurrencyCodes: String? = null
) {

    var allPreferredCountries = extractPreferredCountries(
        cpDataStore.countryList,
        preferredCountryCodes,
        preferredCurrencyCodes
    )
        private set

    val epoxyController by lazy { CountryListController() }

    val controllerData = CountryListControllerData(
        allPreferredCountries,
        cpDataStore.countryList,
        onCountryClickListener,
        cpCountryRowConfig,
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
            allPreferredCountries.filterCountries(query, cpCountryRowConfig)
        controllerData.allCountries =
            cpDataStore.countryList.filterCountries(query, cpCountryRowConfig)
    }

    fun attachRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        epoxyController.setData(controllerData)
        recyclerView.adapter = epoxyController.adapter
    }


    private fun extractPreferredCountries(
        countries: List<CPCountry>,
        preferredCountryCodes: String? = "",
        preferredCurrencyCodes: String? = ""
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

        preferredCurrencyCodes?.split(",")?.map { it.trim() }?.map { currencyCode ->
            result.addAll(countries.filter { it.currencyCode == currencyCode })
        }

        return result.distinctBy { it.alpha2 }
    }


    private fun List<CPCountry>.filterCountries(
        filterQuery: String,
        cpCountryRowConfig: CPCountryRowConfig
    ): List<CPCountry> {
        if (filterQuery.isBlank()) return this
        return this.filter {
            cpCountryRowConfig.mainTextGenerator(it).contains(
                filterQuery,
                true
            ) || (cpCountryRowConfig.secondaryTextGenerator?.invoke(it)?.contains(
                filterQuery,
                true
            ) ?: false)
                    || (cpCountryRowConfig.highlightedTextGenerator?.invoke(it)?.contains(
                filterQuery,
                true
            ) ?: false)
        }
    }
}
