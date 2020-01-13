package com.hbb20.countrypicker

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyRecyclerView
import com.hbb20.CPCountry
import com.hbb20.CPDataStore

object CPRecyclerViewHelper {
    fun load(
        epoxyRecyclerView: EpoxyRecyclerView,
        cpDataStore: CPDataStore,
        preferredCountryCodes: String = "",
        filterQuery: String = "",
        onCountryClickListener: ((CPCountry) -> Unit)
    ) {
        val filteredCountries = filterCountries(cpDataStore.countryList, filterQuery)
        val preferredCountries =
            extractPreferredCountries(filteredCountries, preferredCountryCodes)
        val epoxyModels = mutableListOf<EpoxyModel<*>>()

        //add preferredCountries to RecyclerView
        if (preferredCountries.isNotEmpty()) {
            epoxyModels.addAll(
                preferredCountries.map { country ->
                    CountryRowModel_().id("preferredCountry${country.alpha2Code}")
                        .country(country)
                        .clickListener(onCountryClickListener)
                }
            )
            epoxyModels.add(DividerRowModel_().id("preferredCountryDivider"))
        }

        epoxyModels.addAll(
            filteredCountries.map {
                CountryRowModel_()
                    .id(it.alpha2Code)
                    .country(it)
                    .clickListener(onCountryClickListener)
            }
        )

        epoxyRecyclerView.setModels(epoxyModels)
    }

    fun extractPreferredCountries(
        countries: List<CPCountry>,
        preferredCountryCodes: String
    ): List<CPCountry> {
        return preferredCountryCodes.split(",").map { it.trim() }.mapNotNull { alphaCode ->
            when (alphaCode.length) {
                2 -> countries.find { cpCountry -> cpCountry.alpha2Code.equals(alphaCode, true) }
                3 -> countries.find { cpCountry -> cpCountry.alpha3Code.equals(alphaCode, true) }
                else -> null
            }
        }.distinctBy { it.alpha2Code }
    }

    fun filterCountries(
        countryList: List<CPCountry>,
        filterQuery: String
    ): List<CPCountry> {
        return countryList.filter {
            it.name.contains(filterQuery, true) ||
                    it.englishName.contains(filterQuery, true) ||
                    it.alpha2Code.contains(filterQuery, true) ||
                    it.alpha3Code.contains(filterQuery, true) ||
                    it.phoneCode.toString().contains(filterQuery, true)
        }
    }
}