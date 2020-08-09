package com.hbb20.countrypicker.view

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.config.*
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import com.hbb20.countrypicker.recyclerview.defaultDataStoreModifier

fun Context.prepareCustomCountryPickerView(
    customMasterCountries: String = CPDataStoreGenerator.defaultMasterCountries,
    customExcludedCountries: String = CPDataStoreGenerator.defaultExcludedCountries,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    useCache: Boolean = CPDataStoreGenerator.defaultUseCache,
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
    allowSearch: Boolean = CPDialogConfig.defaultCPDialogAllowSearch,
    allowClearSelection: Boolean = CPDialogConfig.defaultCPDialogAllowClearSelection,
    showTitle: Boolean = CPDialogConfig.defaultCPDialogDefaultShowTitle,
    showFullScreen: Boolean = CPDialogConfig.defaultCPDialogShowFullScreen,
    onCountryChangedListener: ((CPCountry?) -> Unit)? = null
): CPViewHelper {

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

    val cpViewConfig = CPViewConfig(
        initialSelection = initialSelection,
        viewTextGenerator = selectedCountryInfoTextGenerator,
        cpFlagProvider = cpFlagProvider
    )

    val helper =
        CPViewHelper(
            context = this,
            cpDataStore = cpDataStore,
            cpViewConfig = cpViewConfig,
            cpDialogConfig = cpDialogConfig,
            cpListConfig = cpListConfig,
            cpRowConfig = cpCountryRowConfig
        )

    helper.onCountryChangedListener = onCountryChangedListener

    helper.attachViewComponents(
        container = containerViewGroup,
        tvCountryInfo = tvSelectedCountryInfo,
        tvEmojiFlag = tvSelectedCountryEmojiFlag,
        imgFlag = imgSelectedCountryFlag
    )

    return helper
}