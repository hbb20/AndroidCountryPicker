package com.hbb20

import java.text.Collator

const val DEFAULT_FLAG_EMOJI = "\uD83C\uDFF3️️"

data class CPCountry(
    val name: String,
    val englishName: String,
    val alpha2Code: String,
    val alpha3Code: String,
    val flagEmoji: String = DEFAULT_FLAG_EMOJI,
    val phoneCode: Short
) :
        Comparable<CPCountry> {
    override fun compareTo(other: CPCountry): Int {
        return Collator.getInstance().compare(name, other.name)
    }
}