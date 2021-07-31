package com.hbb20.countrypicker.config

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.hbb20.countrypicker.R

data class CPDialogConfig(
    var dialogViewIds: CPDialogViewIds = defaultCPDialogViewIds,
    var allowSearch: Boolean = defaultCPDialogAllowSearch,
    var allowClearSelection: Boolean = defaultCPDialogAllowClearSelection,
    var showTitle: Boolean = defaultCPDialogDefaultShowTitle,
    var showFullScreen: Boolean = defaultCPDialogShowFullScreen,
    var sizeMode: SizeMode = defaultCPDialogDefaultSizeMode,
) {

    companion object {
        const val defaultCPDialogAllowSearch = true
        const val defaultCPDialogShowFullScreen = false
        const val defaultCPDialogAllowClearSelection = false
        const val defaultCPDialogDefaultShowTitle = true
        val defaultCPDialogDefaultSizeMode: SizeMode = SizeMode.Auto

        val defaultCPDialogViewIds =
            CPDialogViewIds(
                R.layout.cp_dialog,
                R.id.clParent,
                R.id.rvCountryList,
                R.id.tvTitle,
                R.id.etQuery,
                R.id.imgClearQuery,
                R.id.btnClearSelection
            )
    }

    private fun getAutoDetectedResizeMode(): SizeMode {
        return if (showFullScreen) {
            SizeMode.Unchanged
        } else {
            SizeMode.Wrap
        }
    }

    fun getApplicableResizeMode(): SizeMode {
        return if (sizeMode == SizeMode.Auto) {
            getAutoDetectedResizeMode()
        } else {
            sizeMode
        }
    }
}

data class CPDialogViewIds(
    @LayoutRes val layoutId: Int,
    @IdRes val containerId: Int,
    @IdRes val recyclerViewId: Int,
    @IdRes val titleTextViewId: Int?,
    @IdRes val queryEditTextId: Int?,
    @IdRes val clearQueryImageViewId: Int?,
    @IdRes val clearSelectionButtonId: Int?
)

sealed class SizeMode {
    object Auto : SizeMode()
    object Unchanged : SizeMode()
    object Wrap : SizeMode()
}
