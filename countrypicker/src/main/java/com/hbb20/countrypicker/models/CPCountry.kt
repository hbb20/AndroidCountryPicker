package com.hbb20.countrypicker.models

import java.text.Collator

const val DEFAULT_FLAG_EMOJI = "\uD83C\uDFF3️️"

data class CPCountry(
    var alpha2: String,
    var alpha3: String,
    var englishName: String,
    var demonym: String,
    var capitalEnglishName: String,
    var areaKM2: String,
    var population: Long?,
    var currencyCode: String,
    var currencyName: String,
    var currencySymbol: String,
    var cctld: String,
    var flagEmoji: String,
    var phoneCode: Short,
    var name: String
) : Comparable<CPCountry> {

    companion object {
        internal fun from(
            countryInfo: CountryInfo,
            translatedName: String?
        ): CPCountry {
            countryInfo.apply {
                return CPCountry(
                    alpha2,
                    alpha3,
                    englishName,
                    demonym,
                    capitalEnglishName,
                    areaKM2,
                    population,
                    currencyCode,
                    currencyName,
                    currencySymbol,
                    cctld,
                    flagEmoji,
                    phoneCode,
                    translatedName ?: englishName
                )
            }
        }
    }

    override fun compareTo(other: CPCountry): Int {
        return Collator.getInstance().compare(name, other.name)
    }
}
