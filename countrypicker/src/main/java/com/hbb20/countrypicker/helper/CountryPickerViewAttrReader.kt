package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.*
import com.hbb20.countrypicker.config.CPInitialSelectionMode
import com.hbb20.countrypicker.config.CPViewConfig
import com.hbb20.countrypicker.config.CPViewConfig.Companion.defaultCPInitialSelectionMode

internal fun readViewConfigFromAttrs(attrs: TypedArray): CPViewConfig {
    val initialSelectionModeIndex =
        attrs.getInt(CountryPickerView_cp_initialSelectionMode, -1)
    val initialSelectionMode =
        if (initialSelectionModeIndex == -1) defaultCPInitialSelectionMode
        else CPInitialSelectionMode.values()[initialSelectionModeIndex]

    val initialCountryAlpha2 = attrs.getString(CountryPickerView_cp_initialSpecificCountry)

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

    return CPViewConfig(initialSelectionMode, initialCountryAlpha2, autoDetectSources)
}