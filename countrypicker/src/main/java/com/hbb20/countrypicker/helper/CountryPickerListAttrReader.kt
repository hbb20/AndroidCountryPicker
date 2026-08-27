package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.CountryPickerView_cpList_preferredCountryCodes
import com.hbb20.countrypicker.config.CPListConfig

internal fun readListConfigFromAttrs(attrs: TypedArray?): CPListConfig {
    return if (attrs == null) {
        CPListConfig()
    } else {
        val preferredCountryCodes =
            attrs.getString(CountryPickerView_cpList_preferredCountryCodes)

        CPListConfig(
            preferredCountryCodes = preferredCountryCodes,
        )
    }
}
