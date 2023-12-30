package com.hbb20.countrypicker.dialog

import android.content.Context
import com.hbb20.countrypicker.config.*
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.flagprovider.CPFlagProvider
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import com.hbb20.countrypicker.recyclerview.defaultDataStoreModifier

fun Context.launchCountryPickerDialog(
    customMasterCountries: String = CPDataStoreGenerator.DEFAULT_MASTER_COUNTRIES,
    customExcludedCountries: String = CPDataStoreGenerator.DEFAULT_EXCLUDED_COUNTRIES,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.DEFAULT_FILE_READER,
    useCache: Boolean = CPDataStoreGenerator.DEFAULT_USE_CACHE,
    customDataStoreModifier: ((CPDataStore) -> (Unit))? = defaultDataStoreModifier,
    cpFlagProvider: CPFlagProvider? = CPRowConfig.defaultFlagProvider,
    primaryTextGenerator: ((CPCountry) -> String) = CPRowConfig.defaultPrimaryTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = null,
    dialogViewIds: CPDialogViewIds = CPDialogConfig.defaultCPDialogViewIds,
    allowSearch: Boolean = CPDialogConfig.DEFAULT_CP_DIALOG_ALLOW_SEARCH,
    allowClearSelection: Boolean = CPDialogConfig.DEFAULT_CP_DIALOG_ALLOW_CLEAR_SELECTION,
    showTitle: Boolean = CPDialogConfig.DEFAULT_CP_DIALOG_SHOW_TITLE,
    showFullScreen: Boolean = CPDialogConfig.DEFAULT_CP_DIALOG_SHOW_FULL_SCREEN,
    sizeMode: SizeMode = CPDialogConfig.DEFAULT_CP_DIALOG_SIZE_MODE,
    onCountryClickListener: ((CPCountry?) -> Unit),
) {
    val cpDataStore =
        CPDataStoreGenerator.generate(
            context = this,
            customMasterCountries = customMasterCountries,
            customExcludedCountries = customExcludedCountries,
            countryFileReader = countryFileReader,
            useCache = useCache,
        )

    customDataStoreModifier?.invoke(cpDataStore)

    val cpDialogConfig =
        CPDialogConfig(
            dialogViewIds = dialogViewIds,
            allowSearch = allowSearch,
            allowClearSelection = allowClearSelection,
            showTitle = showTitle,
            showFullScreen = showFullScreen,
            sizeMode = sizeMode,
        )

    val cpCountryRowConfig =
        CPRowConfig(
            cpFlagProvider = cpFlagProvider,
            primaryTextGenerator = primaryTextGenerator,
            secondaryTextGenerator = secondaryTextGenerator,
            highlightedTextGenerator = highlightedTextGenerator,
        )

    val cpListConfig =
        CPListConfig(
            preferredCountryCodes = preferredCountryCodes,
        )

    val helper =
        CPDialogHelper(
            cpDataStore = cpDataStore,
            cpDialogConfig = cpDialogConfig,
            cpListConfig = cpListConfig,
            cpRowConfig = cpCountryRowConfig,
            onCountryClickListener = onCountryClickListener,
        )

    val dialog = helper.createDialog(this)
    dialog.show()
}
