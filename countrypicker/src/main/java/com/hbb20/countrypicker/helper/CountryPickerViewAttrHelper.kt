package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.CountryPickerView_cpDialog_allowSearch
import com.hbb20.countrypicker.dialog.CPDialogConfig
import com.hbb20.countrypicker.dialog.CPDialogConfig.Companion.defaultCPDialogAllowSearch

internal fun readDialogConfigFromAttrs(attrs: TypedArray): CPDialogConfig {
    val allowSearch =
        attrs.getBoolean(CountryPickerView_cpDialog_allowSearch, defaultCPDialogAllowSearch)
    return CPDialogConfig(allowSearch = allowSearch)
}