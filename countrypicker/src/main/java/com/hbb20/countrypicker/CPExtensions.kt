package com.hbb20.countrypicker

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.*
import com.hbb20.countrypicker.config.CPCountryRowConfig

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
    filterQueryEditText: EditText? = null,
    preferredCountryCodes: String? = null,
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

    val cpRecyclerViewConfig = CPCountryRowConfig(
        rowFontSizeInSP,
        flagProvider,
        mainTextGenerator,
        secondaryTextGenerator,
        highlightedTextGenerator
    )

    val cpRecyclerViewHelper = CPRecyclerViewHelper(
        cpDataStore,
        onCountryClickListener,
        cpRecyclerViewConfig,
        preferredCountryCodes
    )

    cpRecyclerViewHelper.attachRecyclerView(this)
    cpRecyclerViewHelper.attachFilterQueryEditText(filterQueryEditText)
    logMethodEnd("loadCountries")
}