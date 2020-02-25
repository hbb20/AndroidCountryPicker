package com.hbb20.countrypicker.config

import com.hbb20.CPCountry
import com.hbb20.countrypicker.FlagProvider


data class CPRecyclerViewConfig(
    val rowFontSizeInSP: Float = 14f,
    val flagProvider: FlagProvider? = null,
    val rowTextGenerator: ((CPCountry) -> String)? = null,
    val highlightedTextGenerator: ((CPCountry) -> String)? = null
)