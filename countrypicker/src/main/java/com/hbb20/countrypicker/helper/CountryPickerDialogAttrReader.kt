package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.*
import com.hbb20.countrypicker.config.CPDialogConfig
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.defaultCPDialogAllowClearSelection
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.defaultCPDialogAllowSearch
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.defaultCPDialogDefaultShowTitle
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.defaultCPDialogShowFullScreen

internal fun readDialogConfigFromAttrs(attrs: TypedArray?): CPDialogConfig {
    return if (attrs == null) {
        CPDialogConfig()
    } else {
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
                defaultCPDialogDefaultShowTitle
            )

        val showFullScreen =
            attrs.getBoolean(
                CountryPickerView_cpDialog_showFullScreen,
                defaultCPDialogShowFullScreen
            )

        CPDialogConfig(
            allowSearch = allowSearch,
            allowClearSelection = allowClearSelection,
            showTitle = showTitle,
            showFullScreen = showFullScreen
        )
    }
}
