package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.*
import com.hbb20.countrypicker.config.CPViewConfig

internal fun readViewConfigFromAttrs(attrs: TypedArray?): CPViewConfig {
    return if (attrs == null) {
        CPViewConfig()
    } else {
        val initialCountryAlphaCode = attrs.getString(CountryPickerView_cp_initialSpecificCountry)

        val autoDetectSourcesValue = attrs.getInt(CountryPickerView_cp_autoDetectSources, 123)
        val autoDetectSources =
            autoDetectSourcesValue.toString().split("").mapNotNull { it.toIntOrNull() }
                .mapNotNull { sourceIndex ->
                    when (sourceIndex) {
                        1 -> CPCountryDetector.Source.SIM
                        2 -> CPCountryDetector.Source.NETWORK
                        3 -> CPCountryDetector.Source.LOCALE
                        else -> null
                    }
                }

        val initialSelectionModeIndex =
            attrs.getInt(CountryPickerView_cp_initialSelectionMode, -1)
        val initialSelectionMode =
            when (initialSelectionModeIndex) {
                0 -> CPViewConfig.InitialSelection.EmptySelection
                1 -> CPViewConfig.InitialSelection.AutoDetectCountry(autoDetectSources)
                2 -> CPViewConfig.InitialSelection.SpecificCountry(initialCountryAlphaCode ?: "")
                else -> CPViewConfig.defaultCPInitialSelectionMode
            }

        CPViewConfig(initialSelectionMode)
    }
}
