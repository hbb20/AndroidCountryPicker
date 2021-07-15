package com.hbb20.countrypicker.config

import com.hbb20.contrypicker.flagprovider.CPFlagProvider
import com.hbb20.contrypicker.flagprovider.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.models.CPCountry

data class CPRowConfig(
    var cpFlagProvider: CPFlagProvider? = defaultFlagProvider,
    var primaryTextGenerator: ((CPCountry) -> String) = defaultPrimaryTextGenerator,
    var secondaryTextGenerator: ((CPCountry) -> String)? = defaultSecondaryTextGenerator,
    var highlightedTextGenerator: ((CPCountry) -> String)? = defaultHighlightedTextGenerator
) {
    companion object {
        val defaultFlagProvider = DefaultEmojiFlagProvider()
        val defaultPrimaryTextGenerator: ((CPCountry) -> String) = { it.name }
        val defaultSecondaryTextGenerator = null
        val defaultHighlightedTextGenerator = null
    }
}
