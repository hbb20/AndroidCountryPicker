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
        preferredCountryCodes: String = "",
        onCountryClickListener: ((CPCountry) -> Unit),
        cpRecyclerViewConfig: CPRecyclerViewConfig = CPRecyclerViewConfig(),
        queryEditText: EditText? = null
    ) {
        loadForQuery(
            epoxyRecyclerView,
            cpDataStore,
            preferredCountryCodes,
            onCountryClickListener,
            cpRecyclerViewConfig
        )

        queryEditText?.doOnTextChanged { text, _, _, _ ->
            loadForQuery(
                epoxyRecyclerView,
                cpDataStore,
                preferredCountryCodes,
                onCountryClickListener,
                cpRecyclerViewConfig,
                text.toString()
            )
        }
    }

    private fun loadForQuery(
        epoxyRecyclerView: EpoxyRecyclerView,
        cpDataStore: CPDataStore,
        preferredCountryCodes: String = "",
        onCountryClickListener: ((CPCountry) -> Unit),
        cpRecyclerViewConfig: CPRecyclerViewConfig = CPRecyclerViewConfig(),
        filterQuery: String = ""
    ) {

        val filteredCountries = filterCountries(cpDataStore.countryList, filterQuery)
        val preferredCountries =
            extractPreferredCountries(filteredCountries, preferredCountryCodes)
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
        preferredCountryCodes: String
    ): List<CPCountry> {
        return preferredCountryCodes.split(",").map { it.trim() }.mapNotNull { alphaCode ->
            when (alphaCode.length) {
                2 -> countries.find { cpCountry -> cpCountry.alpha2.equals(alphaCode, true) }
                3 -> countries.find { cpCountry -> cpCountry.alpha3.equals(alphaCode, true) }
                else -> null
            }
        }.distinctBy { it.alpha2 }
    }

    fun filterCountries(
        countryList: List<CPCountry>,
        filterQuery: String
    ): List<CPCountry> {
        return countryList.filter {
            it.name.contains(filterQuery, true) ||
                    it.englishName.contains(filterQuery, true) ||
                    it.alpha2.contains(filterQuery, true) ||
                    it.alpha3.contains(filterQuery, true) ||
                    it.phoneCode.toString().contains(filterQuery, true)
        }
    }
}