package com.hbb20

import java.text.Collator

const val DEFAULT_FLAG_EMOJI = "\uD83C\uDFF3️️"

data class CPCountry(
    val alpha2: String,
    val alpha3: String,
    val englishName: String,
    val demonym: String,
    val capitalEnglishName: String,
    val areaKM2: String,
    val population: Long?,
    val currencyCode: String,
    val currencyName: String,
    val currencySymbol: String,
    val cctld: String,
    val flagEmoji: String,
    val phoneCode: Short,
    val name: String
) : Comparable<CPCountry> {

    companion object {
        internal fun from(
            baseCountry: BaseCountry,
            translatedName: String?
        ): CPCountry {
            baseCountry.apply {
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