package com.hbb20.countrypicker

import android.content.Context
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.*
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.config.CPDialogConfig
import com.hbb20.countrypicker.config.CPDialogViewIds
import com.hbb20.countrypicker.config.CPRecyclerViewConfig

val defaultDataStoreModifier = null
fun RecyclerView.loadCountries(
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
    preferredCountryCodes: String? = CPRecyclerViewConfig.defaultPreferredCountryCodes,
    filterQueryEditText: EditText? = null,
    onCountryClickListener: ((CPCountry) -> Unit)
) {
    onMethodBegin("loadCountries")
    val cpDataStore = CPDataStoreGenerator.generate(
        resources,
        customMasterCountries,
        customExcludedCountries,
        countryFileReader,
        useCache
    )

    customDataStoreModifier?.invoke(cpDataStore)

    loadCountriesUsingDataStore(
        cpDataStore,
        rowFontSizeInSP,
        flagProvider,
        mainTextGenerator,
        secondaryTextGenerator,
        highlightedTextGenerator,
        preferredCountryCodes,
        filterQueryEditText,
        onCountryClickListener
    )

    logMethodEnd("loadCountries")
}

fun RecyclerView.loadCountriesUsingDataStore(
    cpDataStore: CPDataStore,
    rowFontSizeInSP: Float = CPCountryRowConfig.defaultFontSize,
    flagProvider: FlagProvider? = CPCountryRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPCountryRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = CPRecyclerViewConfig.defaultPreferredCountryCodes,
    filterQueryEditText: EditText? = null,
    onCountryClickListener: ((CPCountry) -> Unit)
) {
    val cpCountryRowConfig = CPCountryRowConfig(
        rowFontSizeInSP = rowFontSizeInSP,
        flagProvider = flagProvider,
        mainTextGenerator = mainTextGenerator,
        secondaryTextGenerator = secondaryTextGenerator,
        highlightedTextGenerator = highlightedTextGenerator
    )

    val cpRecyclerViewConfig = CPRecyclerViewConfig(preferredCountryCodes = preferredCountryCodes)

    val cpRecyclerViewHelper = CPRecyclerViewHelper(
        cpDataStore = cpDataStore,
        onCountryClickListener = onCountryClickListener,
        cpCountryRowConfig = cpCountryRowConfig,
        cpRecyclerViewConfig = cpRecyclerViewConfig
    )

    cpRecyclerViewHelper.attachRecyclerView(this)
    cpRecyclerViewHelper.attachFilterQueryEditText(filterQueryEditText)
}

fun RecyclerView.loadCountriesUsingDataStoreAndConfig(
    cpDataStore: CPDataStore,

    cpCountryRowConfig: CPCountryRowConfig,
    cpRecyclerViewConfig: CPRecyclerViewConfig,
    filterQueryEditText: EditText? = null,
    onCountryClickListener: (CPCountry) -> Unit
) {

    val cpRecyclerViewHelper = CPRecyclerViewHelper(
        cpDataStore = cpDataStore,
        onCountryClickListener = onCountryClickListener,
        cpCountryRowConfig = cpCountryRowConfig,
        cpRecyclerViewConfig = cpRecyclerViewConfig
    )

    cpRecyclerViewHelper.attachRecyclerView(this)
    cpRecyclerViewHelper.attachFilterQueryEditText(filterQueryEditText)
}

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
    dialogViewIds: CPDialogViewIds = CPDialogConfig.defaultCPDialogViewIds,
    allowSearch: Boolean = CPDialogConfig.defaultCPDialogAllowSearch,
    allowClearSelection: Boolean = CPDialogConfig.defaultCPDialogAllowClearSelection,
    showTitle: Boolean = CPDialogConfig.defaultCPDialogDefaultShowTitle,
    onCountryClickListener: ((CPCountry?) -> Unit)
) {
    val cpDataStore = CPDataStoreGenerator.generate(
        resources = resources,
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
        showTitle = showTitle
    )

    val cpCountryRowConfig = CPCountryRowConfig(
        rowFontSizeInSP = rowFontSizeInSP,
        flagProvider = flagProvider,
        mainTextGenerator = mainTextGenerator,
        secondaryTextGenerator = secondaryTextGenerator,
        highlightedTextGenerator = highlightedTextGenerator
    )

    val cpRecyclerViewConfig = CPRecyclerViewConfig(
        preferredCountryCodes = preferredCountryCodes
    )

    val helper =
        CPDialogHelper(
            cpDataStore,
            onCountryClickListener,
            cpDialogConfig,
            cpRecyclerViewConfig,
            cpCountryRowConfig
        )

    val dialog = helper.createDialog(this)
    dialog.show()
}