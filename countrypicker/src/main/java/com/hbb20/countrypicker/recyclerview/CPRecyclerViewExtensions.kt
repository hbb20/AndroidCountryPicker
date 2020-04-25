package com.hbb20.countrypicker.recyclerview

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.config.CPRecyclerViewConfig
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.logger.logMethodEnd
import com.hbb20.countrypicker.logger.onMethodBegin
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore

val defaultDataStoreModifier = null
fun RecyclerView.loadCountries(
    customMasterCountries: String = CPDataStoreGenerator.defaultMasterCountries,
    customExcludedCountries: String = CPDataStoreGenerator.defaultExcludedCountries,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    useCache: Boolean = CPDataStoreGenerator.defaultUseCache,
    customDataStoreModifier: ((CPDataStore) -> (Unit))? = defaultDataStoreModifier,
    rowFontSizeInSP: Float = CPCountryRowConfig.defaultFontSize,
    CPFlagProvider: CPFlagProvider? = CPCountryRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPCountryRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = CPRecyclerViewConfig.defaultPreferredCountryCodes,
    filterQueryEditText: EditText? = null,
    onCountryClickListener: ((CPCountry) -> Unit)
) {
    onMethodBegin("loadCountries")
    val cpDataStore = CPDataStoreGenerator.generate(
        context,
        customMasterCountries,
        customExcludedCountries,
        countryFileReader,
        useCache
    )

    customDataStoreModifier?.invoke(cpDataStore)

    loadCountriesUsingDataStore(
        cpDataStore,
        rowFontSizeInSP,
        CPFlagProvider,
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
    CPFlagProvider: CPFlagProvider? = CPCountryRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPCountryRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = CPRecyclerViewConfig.defaultPreferredCountryCodes,
    filterQueryEditText: EditText? = null,
    onCountryClickListener: ((CPCountry) -> Unit)
) {
    val cpCountryRowConfig = CPCountryRowConfig(
        rowFontSizeInSP = rowFontSizeInSP,
        CPFlagProvider = CPFlagProvider,
        mainTextGenerator = mainTextGenerator,
        secondaryTextGenerator = secondaryTextGenerator,
        highlightedTextGenerator = highlightedTextGenerator
    )

    val cpRecyclerViewConfig = CPRecyclerViewConfig(preferredCountryCodes = preferredCountryCodes)

    val cpRecyclerViewHelper =
        CPRecyclerViewHelper(
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

    val cpRecyclerViewHelper =
        CPRecyclerViewHelper(
            cpDataStore = cpDataStore,
            onCountryClickListener = onCountryClickListener,
            cpCountryRowConfig = cpCountryRowConfig,
            cpRecyclerViewConfig = cpRecyclerViewConfig
        )

    cpRecyclerViewHelper.attachRecyclerView(this)
    cpRecyclerViewHelper.attachFilterQueryEditText(filterQueryEditText)
}
