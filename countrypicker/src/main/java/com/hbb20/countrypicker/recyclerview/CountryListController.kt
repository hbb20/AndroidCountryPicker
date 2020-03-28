package com.hbb20.countrypicker.recyclerview

import com.airbnb.epoxy.TypedEpoxyController
import com.hbb20.CPCountry
import com.hbb20.CPDataStore
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.countryRow
import com.hbb20.countrypicker.dividerRow
import com.hbb20.countrypicker.noMatchRow

data class CountryListControllerData(
    var preferredCountries: List<CPCountry>,
    var allCountries: List<CPCountry>,
    val onCountryClickListener: ((CPCountry) -> Unit),
    val cpCountryRowConfig: CPCountryRowConfig,
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
                        .recyclerViewConfig(cpCountryRowConfig)
                }
            }

            if (preferredCountries.isNotEmpty()) dividerRow { id("preferred-divider") }

            allCountries.forEach { country ->
                countryRow {
                    id("country${country.alpha2}")
                        .country(country)
                        .clickListener(onCountryClickListener)
                        .recyclerViewConfig(cpCountryRowConfig)
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