package com.hbb20.countrypicker

import com.hbb20.CPCountry

enum class CPInfoUnit(val template: String) {
    NAME("%CountryName%"),
    ALPHA2("%CountryAlpha2%"),
    ALPHA3("%CountryAlpha3%"),
    PHONECODE("%PhoneCode%"),
    ENGLISHNAME("%EnglishName%")
}

object CPTextUtil {
    fun prepare(
        template: String,
        country: CPCountry
    ): String {
        var result = StringBuilder(template).toString()
        result = result.replace(CPInfoUnit.NAME.template, country.name)
        result = result.replace(CPInfoUnit.ALPHA2.template, country.alpha2Code)
        result = result.replace(CPInfoUnit.ALPHA3.template, country.alpha3Code)
        result = result.replace(CPInfoUnit.PHONECODE.template, "${country.phoneCode}")
        result = result.replace(CPInfoUnit.ENGLISHNAME.template, "${country.englishName}")

        return result
    }
}