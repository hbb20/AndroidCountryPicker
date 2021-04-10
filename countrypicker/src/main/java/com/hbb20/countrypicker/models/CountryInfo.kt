package com.hbb20.countrypicker.models

internal data class CountryInfo(
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
    val phoneCode: Short
)