package com.hbb20.countrypicker

import android.content.res.Resources
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CPCountry
import com.hbb20.CPDataStore
import com.hbb20.CPDataStoreGenerator
import com.hbb20.CountryFileReading
import com.hbb20.countrypicker.config.CPCountryRowConfig

fun RecyclerView.loadCountries(
    resources: Resources,
    customMasterCountries: String = CPDataStoreGenerator.defaultMasterCountries,
    customExcludedCountries: String = CPDataStoreGenerator.defaultExcludedCountries,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    useCache: Boolean = CPDataStoreGenerator.defaultUseCache,
    customDataStoreModifier: ((CPDataStore) -> (Unit))? = CPDataStoreGenerator.defaultDataStoreModifier,
    rowFontSizeInSP: Float = CPCountryRowConfig.defaultFontSize,
    flagProvider: FlagProvider? = CPCountryRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPCountryRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPCountryRowConfig.defaultHighlightedTextGenerator,
    filterQueryEditText: EditText? = null,
    preferredCountryCodes: String? = null,
    preferredCurrencyCodes: String? = null,
    onCountryClickListener: ((CPCountry) -> Unit)
) {
    val cpDataStore = CPDataStoreGenerator.generate(
        resources,
        customMasterCountries,
        customExcludedCountries,
        countryFileReader,
        useCache,
        customDataStoreModifier
    )

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
        preferredCountryCodes,
        preferredCurrencyCodes
    )

    cpRecyclerViewHelper.attachRecyclerView(this)
    cpRecyclerViewHelper.attachFilterQueryEditText(filterQueryEditText)
}