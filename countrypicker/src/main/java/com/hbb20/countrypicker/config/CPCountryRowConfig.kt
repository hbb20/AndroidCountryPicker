package com.hbb20.countrypicker.config

import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.models.CPCountry


data class CPCountryRowConfig(
    var CPFlagProvider: CPFlagProvider? = defaultFlagProvider,
    var mainTextGenerator: ((CPCountry) -> String) = defaultMainTextGenerator,
    var secondaryTextGenerator: ((CPCountry) -> String)? = defaultSecondaryTextGenerator,
    var highlightedTextGenerator: ((CPCountry) -> String)? = defaultHighlightedTextGenerator
) {
    companion object {
        val defaultFlagProvider = DefaultEmojiFlagProvider()
        val defaultMainTextGenerator: ((CPCountry) -> String) = { it.name }
        val defaultSecondaryTextGenerator = null
        val defaultHighlightedTextGenerator = null
    }
}