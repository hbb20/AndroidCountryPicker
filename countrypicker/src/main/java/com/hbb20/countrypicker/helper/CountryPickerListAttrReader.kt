package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.CountryPickerView_cpList_preferredCountryCodes
import com.hbb20.countrypicker.config.CPListConfig

internal fun readListConfigFromAttrs(attrs: TypedArray): CPListConfig {
    val preferredCountryCodes =
        attrs.getString(CountryPickerView_cpList_preferredCountryCodes)

    return CPListConfig(
        preferredCountryCodes = preferredCountryCodes
    )
}