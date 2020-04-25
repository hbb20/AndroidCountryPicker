package com.hbb20.countrypicker.dialog

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.hbb20.countrypicker.R


data class CPDialogConfig(
    var dialogViewIds: CPDialogViewIds = defaultCPDialogViewIds,
    var allowSearch: Boolean = defaultCPDialogAllowSearch,
    var allowClearSelection: Boolean = defaultCPDialogAllowClearSelection,
    var showTitle: Boolean = defaultCPDialogDefaultShowTitle,
    var showFullScreen: Boolean = defaultCPDialogShowFullScreen
) {
    companion object {
        const val defaultCPDialogAllowSearch = true
        const val defaultCPDialogShowFullScreen = false
        const val defaultCPDialogAllowClearSelection = false
        const val defaultCPDialogDefaultShowTitle = true
        val defaultCPDialogViewIds =
            CPDialogViewIds(
                R.layout.cp_dialog,
                R.id.rvCountryList,
                R.id.tvTitle,
                R.id.etQuery,
                R.id.imgClearQuery,
                R.id.btnClearSelection
            )
    }
}

data class CPDialogViewIds(
    @LayoutRes val layoutId: Int,
    @IdRes val recyclerViewId: Int,
    @IdRes val titleTextViewId: Int?,
    @IdRes val queryEditTextId: Int?,
    @IdRes val clearQueryImageViewId: Int?,
    @IdRes val clearSelectionButtonId: Int?
)