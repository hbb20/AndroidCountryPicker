package com.hbb20.countrypicker.recyclerview

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.countrypicker.CPFlagProvider
import com.hbb20.countrypicker.config.CPListConfig
import com.hbb20.countrypicker.config.CPRowConfig
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
    CPFlagProvider: CPFlagProvider? = CPRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = CPListConfig.defaultCPListPreferredCountryCodes,
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
    CPFlagProvider: CPFlagProvider? = CPRowConfig.defaultFlagProvider,
    mainTextGenerator: ((CPCountry) -> String) = CPRowConfig.defaultMainTextGenerator,
    secondaryTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultSecondaryTextGenerator,
    highlightedTextGenerator: ((CPCountry) -> String)? = CPRowConfig.defaultHighlightedTextGenerator,
    preferredCountryCodes: String? = CPListConfig.defaultCPListPreferredCountryCodes,
    filterQueryEditText: EditText? = null,
    onCountryClickListener: ((CPCountry) -> Unit)
) {
    val cpCountryRowConfig = CPRowConfig(
        CPFlagProvider = CPFlagProvider,
        mainTextGenerator = mainTextGenerator,
        secondaryTextGenerator = secondaryTextGenerator,
        highlightedTextGenerator = highlightedTextGenerator
    )

    val cpListConfig = CPListConfig(preferredCountryCodes = preferredCountryCodes)

    val cpRecyclerViewHelper =
        CPRecyclerViewHelper(
            cpDataStore = cpDataStore,
            onCountryClickListener = onCountryClickListener,
            cpRowConfig = cpCountryRowConfig,
            cpListConfig = cpListConfig
        )

    cpRecyclerViewHelper.attachRecyclerView(this)
    cpRecyclerViewHelper.attachFilterQueryEditText(filterQueryEditText)
}

fun RecyclerView.loadCountriesUsingDataStoreAndConfig(
    cpDataStore: CPDataStore,
    cpRowConfig: CPRowConfig,
    cpListConfig: CPListConfig,
    filterQueryEditText: EditText? = null,
    onCountryClickListener: (CPCountry) -> Unit
) {

    val cpRecyclerViewHelper =
        CPRecyclerViewHelper(
            cpDataStore = cpDataStore,
            onCountryClickListener = onCountryClickListener,
            cpRowConfig = cpRowConfig,
            cpListConfig = cpListConfig
        )

    cpRecyclerViewHelper.attachRecyclerView(this)
    cpRecyclerViewHelper.attachFilterQueryEditText(filterQueryEditText)
}
