package com.hbb20

internal data class BaseCountry(
    val alpha2: String,
    val alpha3: String,
    val englishName: String,
    val flagEmoji: String = DEFAULT_FLAG_EMOJI,
    val phoneCode: Short
)