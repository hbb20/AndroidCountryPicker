package com.hbb20.countrypicker

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyRecyclerView
import com.hbb20.CPCountry
import com.hbb20.CPDataStore
import com.hbb20.countrypicker.config.CPRecyclerViewConfig

object CPRecyclerViewHelper {
    fun load(
        epoxyRecyclerView: EpoxyRecyclerView,
        cpDataStore: CPDataStore,
        preferredCountryCodes: String? = null,
        preferredCurrencyCodes: String? = null,
        onCountryClickListener: ((CPCountry) -> Unit),
        cpRecyclerViewConfig: CPRecyclerViewConfig = CPRecyclerViewConfig(),
        queryEditText: EditText? = null
    ) {
        loadForQuery(
            epoxyRecyclerView,
            cpDataStore,
            preferredCountryCodes,
            preferredCurrencyCodes,
            onCountryClickListener,
            cpRecyclerViewConfig
        )

        queryEditText?.doOnTextChanged { text, _, _, _ ->
            loadForQuery(
                epoxyRecyclerView,
                cpDataStore,
                preferredCountryCodes,
                preferredCurrencyCodes,
                onCountryClickListener,
                cpRecyclerViewConfig,
                text.toString()
            )
        }
    }

    private fun loadForQuery(
        epoxyRecyclerView: EpoxyRecyclerView,
        cpDataStore: CPDataStore,
        preferredCountryCodes: String? = "",
        preferredCurrencyCodes: String? = "",
        onCountryClickListener: ((CPCountry) -> Unit),
        cpRecyclerViewConfig: CPRecyclerViewConfig = CPRecyclerViewConfig(),
        filterQuery: String = ""
    ) {

        val filteredCountries =
            filterCountries(cpDataStore.countryList, filterQuery, cpRecyclerViewConfig)
        val preferredCountries =
            extractPreferredCountries(
                filteredCountries,
                preferredCountryCodes,
                preferredCurrencyCodes
            )
        val epoxyModels = mutableListOf<EpoxyModel<*>>()

        //add preferredCountries to RecyclerView
        if (preferredCountries.isNotEmpty()) {
            epoxyModels.addAll(
                preferredCountries.map { country ->
                    CountryRowModel_().id("preferredCountry${country.alpha2}")
                        .country(country)
                        .clickListener(onCountryClickListener)
                        .recyclerViewConfig(cpRecyclerViewConfig)
                }
            )
            epoxyModels.add(DividerRowModel_().id("preferredCountryDivider"))
        }

        //add all other countries
        epoxyModels.addAll(
            filteredCountries.map {
                CountryRowModel_()
                    .id(it.alpha2)
                    .country(it)
                    .clickListener(onCountryClickListener)
                    .recyclerViewConfig(cpRecyclerViewConfig)
            }
        )

        //if filtered countries is empty, add no match ack row
        if (filteredCountries.isEmpty()) {
            epoxyModels.add(
                NoMatchRowModel_()
                    .id("noResultAck")
                    .noMatchAckText(cpDataStore.messageGroup.noMatchMsg)
            )
        }

        epoxyRecyclerView.setModels(epoxyModels)
    }

    fun extractPreferredCountries(
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

    fun filterCountries(
        countryList: List<CPCountry>,
        filterQuery: String,
        cpRecyclerViewConfig: CPRecyclerViewConfig
    ): List<CPCountry> {
        if (filterQuery.isBlank()) return countryList
        return countryList.filter {

            cpRecyclerViewConfig.mainTextGenerator(it).contains(
                filterQuery,
                true
            ) || (cpRecyclerViewConfig.secondaryTextGenerator?.invoke(it)?.contains(
                filterQuery,
                true
            ) ?: false)
                    || (cpRecyclerViewConfig.highlightedTextGenerator?.invoke(it)?.contains(
                filterQuery,
                true
            ) ?: false)
        }
    }
}