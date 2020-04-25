package com.hbb20.countrypicker.dialog

import android.content.Context
import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.config.CPRecyclerViewConfig
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import com.hbb20.countrypicker.recyclerview.defaultDataStoreModifier


fun Context.launchCountryPickerDialog(
    customMasterCountries: String = CPDataStoreGenerator.defaultMasterCountries,
    customExcludedCountries: String = CPDataStoreGenerator.defaultExcludedCountries,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    useCache: Boolean = CPDataStoreGenerator.defaultUseCache,
    customDataStoreModifier: ((CPDataStore) -> (Unit))? = defaultDataStoreModifier,
    CPFlagProvider: CPFlagProvider? = CPCountryRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPCountryRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = null,
    dialogViewIds: CPDialogViewIds = CPDialogConfig.defaultCPDialogViewIds,
    allowSearch: Boolean = CPDialogConfig.defaultCPDialogAllowSearch,
    allowClearSelection: Boolean = CPDialogConfig.defaultCPDialogAllowClearSelection,
    showTitle: Boolean = CPDialogConfig.defaultCPDialogDefaultShowTitle,
    showFullScreen: Boolean = CPDialogConfig.defaultCPDialogShowFullScreen,
    onCountryClickListener: ((CPCountry?) -> Unit)
) {
    val cpDataStore = CPDataStoreGenerator.generate(
        context = this,
        customMasterCountries = customMasterCountries,
        customExcludedCountries = customExcludedCountries,
        countryFileReader = countryFileReader,
        useCache = useCache
    )

    customDataStoreModifier?.invoke(cpDataStore)

    val cpDialogConfig = CPDialogConfig(
        dialogViewIds = dialogViewIds,
        allowSearch = allowSearch,
        allowClearSelection = allowClearSelection,
        showTitle = showTitle,
        showFullScreen = showFullScreen
    )

    val cpCountryRowConfig = CPCountryRowConfig(
        CPFlagProvider = CPFlagProvider,
        mainTextGenerator = mainTextGenerator,
        secondaryTextGenerator = secondaryTextGenerator,
        highlightedTextGenerator = highlightedTextGenerator
    )

    val cpRecyclerViewConfig = CPRecyclerViewConfig(
        preferredCountryCodes = preferredCountryCodes
    )

    val helper =
        CPDialogHelper(
            cpDataStore = cpDataStore,
            cpDialogConfig = cpDialogConfig,
            cpRecyclerViewConfig = cpRecyclerViewConfig,
            cpCountryRowConfig = cpCountryRowConfig,
            onCountryClickListener = onCountryClickListener
        )

    val dialog = helper.createDialog(this)
    dialog.show()
}