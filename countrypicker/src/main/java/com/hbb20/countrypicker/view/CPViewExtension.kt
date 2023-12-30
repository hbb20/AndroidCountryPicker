package com.hbb20.countrypicker.view

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hbb20.countrypicker.config.*
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.flagprovider.CPFlagProvider
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import com.hbb20.countrypicker.recyclerview.defaultDataStoreModifier

fun Context.prepareCustomCountryPickerView(
    customMasterCountries: String = CPDataStoreGenerator.DEFAULT_MASTER_COUNTRIES,
    customExcludedCountries: String = CPDataStoreGenerator.DEFAULT_EXCLUDED_COUNTRIES,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.DEFAULT_FILE_READER,
    useCache: Boolean = CPDataStoreGenerator.DEFAULT_USE_CACHE,
    customDataStoreModifier: ((CPDataStore) -> (Unit))? = defaultDataStoreModifier,
    containerViewGroup: ViewGroup,
    tvSelectedCountryInfo: TextView,
    tvSelectedCountryEmojiFlag: TextView? = null,
    imgSelectedCountryFlag: ImageView? = null,
    initialSelection: CPViewConfig.InitialSelection = CPViewConfig.defaultCPInitialSelectionMode,
    selectedCountryInfoTextGenerator: ((CPCountry) -> String) = CPViewConfig.defaultSelectedCountryInfoTextGenerator,
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
    onCountryChangedListener: ((CPCountry?) -> Unit)? = null,
): CPViewHelper {
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

    val cpViewConfig =
        CPViewConfig(
            initialSelection = initialSelection,
            viewTextGenerator = selectedCountryInfoTextGenerator,
            cpFlagProvider = cpFlagProvider,
        )

    val helper =
        CPViewHelper(
            context = this,
            cpDataStore = cpDataStore,
            cpViewConfig = cpViewConfig,
            cpDialogConfig = cpDialogConfig,
            cpListConfig = cpListConfig,
            cpRowConfig = cpCountryRowConfig,
        )

    helper.onCountryChangedListener = onCountryChangedListener

    helper.attachViewComponents(
        container = containerViewGroup,
        tvCountryInfo = tvSelectedCountryInfo,
        tvEmojiFlag = tvSelectedCountryEmojiFlag,
        imgFlag = imgSelectedCountryFlag,
    )

    return helper
}
