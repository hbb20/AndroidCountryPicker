package com.hbb20.countrypicker

import java.text.Collator

data class CPCountry(val name: String, val englishName: String, val nameCode: String, val phoneCode: Int) :
    Comparable<CPCountry> {
    override fun compareTo(other: CPCountry): Int {
        return Collator.getInstance().compare(name, other.name)
    }
}

internal data class BaseCountry(
    val aplha2: String,
    val alpha3: String,
    val englishName: String,
    val phoneCode: Short
)