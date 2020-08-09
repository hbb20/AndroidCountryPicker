package com.hbb20.countrypicker.config

import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.helper.CPCountryDetector.Source
import com.hbb20.countrypicker.models.CPCountry

data class CPViewConfig(
    val initialSelection: InitialSelection = defaultCPInitialSelectionMode,
    var viewTextGenerator: ((CPCountry) -> String) = defaultSelectedCountryInfoTextGenerator,
    var cpFlagProvider: CPFlagProvider? = DefaultEmojiFlagProvider()
) {
    companion object {
        val defaultCPInitialSelectionMode = InitialSelection.EmptySelection
        val defaultCountryDetectorSources = listOf(Source.SIM, Source.NETWORK, Source.LOCALE)
        val defaultSelectedCountryInfoTextGenerator: ((CPCountry) -> String) = { it.name }
    }

    sealed class InitialSelection {
        object EmptySelection : InitialSelection()

        class AutoDetectCountry(val autoDetectSources: List<Source> = defaultCountryDetectorSources) :
                InitialSelection()

        class SpecificCountry(val countryCode: String) : InitialSelection()
    }
}