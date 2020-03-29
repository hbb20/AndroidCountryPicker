package com.hbb20.countrypicker.config

import com.hbb20.CPCountry
import com.hbb20.countrypicker.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.FlagProvider


data class CPCountryRowConfig(
    val rowFontSizeInSP: Float = defaultFontSize,
    val flagProvider: FlagProvider? = defaultFlagProvider,
    val mainTextGenerator: ((CPCountry) -> String) = defaultMainTextGenerator,
    val secondaryTextGenerator: ((CPCountry) -> String)? = defaultSecondaryTextGenerator,
    val highlightedTextGenerator: ((CPCountry) -> String)? = defaultHighlightedTextGenerator
) {
    companion object {
        const val defaultFontSize = 14f
        val defaultFlagProvider = DefaultEmojiFlagProvider()
        val defaultMainTextGenerator: ((CPCountry) -> String) = { it.name }
        val defaultSecondaryTextGenerator = null
        val defaultHighlightedTextGenerator = null
    }
}