package com.hbb20.countrypicker.config

import com.hbb20.countrypicker.CPInfoUnit
import com.hbb20.countrypicker.FlagProvider


data class CPRecyclerViewConfig(
    val rowFontSizeInSP: Float = 14f,
    val flagProvider: FlagProvider? = null,
    val rowText: String = "${CPInfoUnit.NAME.template}"
)