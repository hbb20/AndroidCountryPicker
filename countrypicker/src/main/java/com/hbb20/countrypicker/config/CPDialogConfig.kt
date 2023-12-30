package com.hbb20.countrypicker.config

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.hbb20.countrypicker.R

data class CPDialogConfig(
    var dialogViewIds: CPDialogViewIds = defaultCPDialogViewIds,
    var allowSearch: Boolean = DEFAULT_CP_DIALOG_ALLOW_SEARCH,
    var allowClearSelection: Boolean = DEFAULT_CP_DIALOG_ALLOW_CLEAR_SELECTION,
    var showTitle: Boolean = DEFAULT_CP_DIALOG_SHOW_TITLE,
    var showFullScreen: Boolean = DEFAULT_CP_DIALOG_SHOW_FULL_SCREEN,
    var sizeMode: SizeMode = DEFAULT_CP_DIALOG_SIZE_MODE,
) {
    companion object {
        const val DEFAULT_CP_DIALOG_ALLOW_SEARCH = true
        const val DEFAULT_CP_DIALOG_SHOW_FULL_SCREEN = false
        const val DEFAULT_CP_DIALOG_ALLOW_CLEAR_SELECTION = false
        const val DEFAULT_CP_DIALOG_SHOW_TITLE = true
        val DEFAULT_CP_DIALOG_SIZE_MODE: SizeMode = SizeMode.Auto

        val defaultCPDialogViewIds =
            CPDialogViewIds(
                R.layout.cp_dialog,
                R.id.clParent,
                R.id.rvCountryList,
                R.id.tvTitle,
                R.id.etQuery,
                R.id.imgClearQuery,
                R.id.btnClearSelection,
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
    @IdRes val clearSelectionButtonId: Int?,
)

sealed class SizeMode {
    object Auto : SizeMode()

    object Unchanged : SizeMode()

    object Wrap : SizeMode()
}
