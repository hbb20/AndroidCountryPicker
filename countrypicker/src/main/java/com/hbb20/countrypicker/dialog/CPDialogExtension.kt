package com.hbb20.countrypicker.dialog

import android.content.Context
import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.config.CPDialogConfig
import com.hbb20.countrypicker.config.CPDialogViewIds
import com.hbb20.countrypicker.config.CPListConfig
import com.hbb20.countrypicker.config.CPRowConfig
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
    cpFlagProvider: CPFlagProvider? = CPRowConfig.defaultFlagProvider,
    primaryTextGenerator: ((CPCountry) -> String) = CPRowConfig.defaultPrimaryTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultHighlightedTextGenerator,
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

    val cpCountryRowConfig = CPRowConfig(
        cpFlagProvider = cpFlagProvider,
        primaryTextGenerator = primaryTextGenerator,
        secondaryTextGenerator = secondaryTextGenerator,
        highlightedTextGenerator = highlightedTextGenerator
    )

    val cpListConfig = CPListConfig(
        preferredCountryCodes = preferredCountryCodes
    )

    val helper =
        CPDialogHelper(
            cpDataStore = cpDataStore,
            cpDialogConfig = cpDialogConfig,
            cpListConfig = cpListConfig,
            cpRowConfig = cpCountryRowConfig,
            onCountryClickListener = onCountryClickListener
        )

    val dialog = helper.createDialog(this)
    dialog.show()
}
