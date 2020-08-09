package com.hbb20.countrypicker.config

import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.helper.CPCountryDetector.Source

enum class CPInitialSelectionMode {
    Empty,
    AutoDetectCountry,
    SpecificCountry
}

data class CPViewConfig(
    val initialSelectionMode: CPInitialSelectionMode = defaultCPInitialSelectionMode,
    val initialSpecificCountry: String? = defaultCPInitialCountryCode,
    val countryDetectSources: List<Source> = defaultCountryDetectorSources,
    val cpFlagProvider: CPFlagProvider? = DefaultEmojiFlagProvider()
) {
    companion object {
        val defaultCPInitialSelectionMode = CPInitialSelectionMode.Empty
        val defaultCPInitialCountryCode = null
        val defaultCountryDetectorSources = listOf(Source.SIM, Source.NETWORK, Source.LOCALE)
    }
}