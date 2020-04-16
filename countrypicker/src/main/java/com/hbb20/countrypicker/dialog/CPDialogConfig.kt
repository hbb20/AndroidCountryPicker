package com.hbb20.countrypicker.dialog

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.hbb20.countrypicker.R


data class CPDialogConfig(
    val dialogViewIds: CPDialogViewIds = defaultCPDialogViewIds,
    val allowSearch: Boolean = defaultCPDialogAllowSearch,
    val allowClearSelection: Boolean = defaultCPDialogAllowClearSelection,
    val showTitle: Boolean = defaultCPDialogDefaultShowTitle,
    val showFullScreen: Boolean = defaultCPDialogShowFullScreen
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