package com.hbb20.countrypicker.recyclerview

import com.airbnb.epoxy.TypedEpoxyController
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore

data class CountryListControllerData(
    var preferredCountries: List<CPCountry>,
    var allCountries: List<CPCountry>,
    val onCountryClickListener: ((CPCountry) -> Unit),
    val cpRowConfig: CPRowConfig,
    val cpDataStore: CPDataStore
)

class CountryListController :
        TypedEpoxyController<CountryListControllerData>() {

    override fun buildModels(
        data: CountryListControllerData
    ) {
        data.apply {
            preferredCountries.forEach { country ->
                countryRow {
                    id("preferredCountry${country.alpha2}")
                        .country(country)
                        .clickListener(onCountryClickListener)
                        .rowConfig(cpRowConfig)
                }
            }

            if (preferredCountries.isNotEmpty()) dividerRow { id("preferred-divider") }

            allCountries.forEach { country ->
                countryRow {
                    id("country${country.alpha2}")
                        .country(country)
                        .clickListener(onCountryClickListener)
                        .rowConfig(cpRowConfig)
                }
            }

            if (preferredCountries.isEmpty() && allCountries.isEmpty()) {
                noMatchRow {
                    id("noResultAck")
                        .noMatchAckText(cpDataStore.messageGroup.noMatchMsg)
                }
            }
        }

    }
}