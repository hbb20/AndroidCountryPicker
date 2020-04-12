package com.hbb20.countrypicker

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CPCountry
import com.hbb20.CPDataStore
import com.hbb20.CPDataStoreGenerator
import com.hbb20.CountryFileReading
import com.hbb20.countrypicker.config.CPCountryRowConfig

data class CPDialogViewIds(@LayoutRes val layoutId: Int, @IdRes val recyclerViewId: Int, @IdRes val titleTextViewId: Int?, @IdRes val queryEditTextId: Int?, @IdRes val clearQueryImageViewId: Int?, @IdRes val clearSelectionButtonId: Int?)

val defaultCPDialogViewIds = CPDialogViewIds(
    R.layout.cp_dialog,
    R.id.rvCountryList,
    R.id.tvTitle,
    R.id.etQuery,
    R.id.imgClearQuery,
    R.id.btnClearSelection
)
const val defaultCPDialogAllowSearch = true
const val defaultCPDialogAllowClearSelection = false
const val defaultCPDialogDefaultShowTitle = true
fun Context.launchCountryPickerDialog(
    customMasterCountries: String = CPDataStoreGenerator.defaultMasterCountries,
    customExcludedCountries: String = CPDataStoreGenerator.defaultExcludedCountries,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    useCache: Boolean = CPDataStoreGenerator.defaultUseCache,
    customDataStoreModifier: ((CPDataStore) -> (Unit))? = defaultDataStoreModifier,
    rowFontSizeInSP: Float = CPCountryRowConfig.defaultFontSize,
    flagProvider: FlagProvider? = CPCountryRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPCountryRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = null,
    dialogViewIds: CPDialogViewIds = defaultCPDialogViewIds,
    allowSearch: Boolean = defaultCPDialogAllowSearch,
    allowClearSelection: Boolean = defaultCPDialogAllowClearSelection,
    showTitle: Boolean = defaultCPDialogDefaultShowTitle,
    onCountryClickListener: ((CPCountry?) -> Unit)
) {
    val cpDataStore = CPDataStoreGenerator.generate(
        resources,
        customMasterCountries,
        customExcludedCountries,
        countryFileReader,
        useCache
    )

    customDataStoreModifier?.invoke(cpDataStore)

    //view
    val dialog = Dialog(this)
    dialog.setCancelable(true)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val dialogView = LayoutInflater.from(this).inflate(dialogViewIds.layoutId, null, false)
    dialog.window?.setContentView(dialogView)
    val recyclerView: RecyclerView = dialogView.findViewById(dialogViewIds.recyclerViewId)
    val etQuery: EditText? =
        dialogViewIds.queryEditTextId?.let { etQueryId -> dialogView.findViewById(etQueryId) }
    val imgClearQuery: ImageView? = dialogViewIds.clearQueryImageViewId?.let { imgClearQueryId ->
        dialogView.findViewById(imgClearQueryId)
    }
    val tvTitle: TextView? = dialogViewIds.titleTextViewId?.let { tvTitleId ->
        dialogView.findViewById(tvTitleId)
    }
    val btnClearSelection: Button? =
        dialogViewIds.clearSelectionButtonId?.let { btnClearSelectionId ->
            dialogView.findViewById(btnClearSelectionId)
        }

    // configuration
    fun refreshClearQueryButton() {
        imgClearQuery?.isVisible = etQuery?.text?.isNotEmpty() ?: false
    }
    tvTitle?.text = cpDataStore.messageGroup.dialogTitle
    tvTitle?.isVisible = showTitle
    btnClearSelection?.text = cpDataStore.messageGroup.clearSelectionText
    btnClearSelection?.setOnClickListener {
        onCountryClickListener(null)
        dialog.dismiss()
    }
    btnClearSelection?.isVisible = allowClearSelection
    imgClearQuery?.setOnClickListener {
        etQuery?.setText("")
    }
    etQuery?.isVisible = allowSearch
    etQuery?.doOnTextChanged { _, _, _, _ -> refreshClearQueryButton() }

    recyclerView.loadCountries(
        customMasterCountries,
        customExcludedCountries,
        countryFileReader,
        useCache,
        customDataStoreModifier,
        rowFontSizeInSP,
        flagProvider,
        mainTextGenerator,
        secondaryTextGenerator,
        highlightedTextGenerator,
        etQuery,
        preferredCountryCodes
    ) { selectedCountry ->
        onCountryClickListener(selectedCountry)
        dialog.dismiss()
    }

    dialog.show()
    //    dialog.window?.setLayout(
    //        ViewGroup.LayoutParams.MATCH_PARENT,
    //        ViewGroup.LayoutParams.MATCH_PARENT
    //    )

}