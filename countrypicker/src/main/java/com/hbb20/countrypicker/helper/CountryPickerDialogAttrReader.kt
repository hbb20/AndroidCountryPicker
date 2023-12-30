package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.*
import com.hbb20.countrypicker.config.CPDialogConfig
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.DEFAULT_CP_DIALOG_ALLOW_CLEAR_SELECTION
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.DEFAULT_CP_DIALOG_ALLOW_SEARCH
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.DEFAULT_CP_DIALOG_SHOW_FULL_SCREEN
import com.hbb20.countrypicker.config.CPDialogConfig.Companion.DEFAULT_CP_DIALOG_SHOW_TITLE

internal fun readDialogConfigFromAttrs(attrs: TypedArray?): CPDialogConfig {
    return if (attrs == null) {
        CPDialogConfig()
    } else {
        val allowSearch =
            attrs.getBoolean(CountryPickerView_cpDialog_allowSearch, DEFAULT_CP_DIALOG_ALLOW_SEARCH)

        val allowClearSelection =
            attrs.getBoolean(
                CountryPickerView_cpDialog_allowClearSelection,
                DEFAULT_CP_DIALOG_ALLOW_CLEAR_SELECTION,
            )

        val showTitle =
            attrs.getBoolean(
                CountryPickerView_cpDialog_showTitle,
                DEFAULT_CP_DIALOG_SHOW_TITLE,
            )

        val showFullScreen =
            attrs.getBoolean(
                CountryPickerView_cpDialog_showFullScreen,
                DEFAULT_CP_DIALOG_SHOW_FULL_SCREEN,
            )

        CPDialogConfig(
            allowSearch = allowSearch,
            allowClearSelection = allowClearSelection,
            showTitle = showTitle,
            showFullScreen = showFullScreen,
        )
    }
}
