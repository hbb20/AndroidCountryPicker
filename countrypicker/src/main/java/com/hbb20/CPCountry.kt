package com.hbb20

import java.text.Collator

data class CPCountry(
    val name: String,
    val englishName: String,
    val alpha2Code: String,
    val alpha3Code: String,
    val phoneCode: Short
) :
        Comparable<CPCountry> {
    override fun compareTo(other: CPCountry): Int {
        return Collator.getInstance().compare(name, other.name)
    }
}