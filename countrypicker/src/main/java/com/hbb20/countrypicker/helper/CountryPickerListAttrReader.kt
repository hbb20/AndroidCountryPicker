package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.*
import com.hbb20.countrypicker.dialog.CPDialogConfig
import com.hbb20.countrypicker.dialog.CPDialogConfig.Companion.defaultCPDialogAllowClearSelection
import com.hbb20.countrypicker.dialog.CPDialogConfig.Companion.defaultCPDialogAllowSearch
import com.hbb20.countrypicker.dialog.CPDialogConfig.Companion.defaultCPDialogShowFullScreen

internal fun readListConfigFromAttrs(attrs: TypedArray): CPDialogConfig {
    val allowSearch =
        attrs.getBoolean(CountryPickerView_cpDialog_allowSearch, defaultCPDialogAllowSearch)

    val allowClearSelection =
        attrs.getBoolean(
            CountryPickerView_cpDialog_allowClearSelection,
            defaultCPDialogAllowClearSelection
        )

    val showTitle =
        attrs.getBoolean(
            CountryPickerView_cpDialog_showTitle,
            defaultCPDialogAllowClearSelection
        )

    val showFullScreen =
        attrs.getBoolean(CountryPickerView_cpDialog_showFullScreen, defaultCPDialogShowFullScreen)

    return CPDialogConfig(
        allowSearch = allowSearch,
        allowClearSelection = allowClearSelection,
        showTitle = showTitle,
        showFullScreen = showFullScreen
    )
}