package com.hbb20

import android.content.res.Resources
import com.hbb20.countrypicker.CPRecyclerViewHelper
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CPRecyclerViewHelperTest {

    private val resources = mock<Resources> {}
    private val dataStore =
        CPDataStoreGenerator.generate(resources, countryFileReader = MockCountryFileReader)

    @Test
    fun preferredCountriesWorkWithAlpha2() {

        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "IN,AU",
            onCountryClickListener = mock()
        )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun trimCodeBeforePreferred() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "IN ,AU",
            onCountryClickListener = mock()
        )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun preferredCountriesWorkWithAlpha3() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "IND,AUS",
            onCountryClickListener = mock()
        )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun preferredCountriesWorkWithAlpha2and3() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "IN,AUS",
            onCountryClickListener = mock()
        )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun preferredCurrencyCodes() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCurrencyCodes = "INR,USD",
            onCountryClickListener = mock()
        )

        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("US", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun removeDuplicateFromPreferredCurrencyCodesAndCountryCodes() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "IN,LK",
            preferredCurrencyCodes = "INR,USD",
            onCountryClickListener = mock()
        )

        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("LK", helper.allPreferredCountries[1].alpha2)
        assertEquals("US", helper.allPreferredCountries[2].alpha2)
    }

    @Test
    fun invalidCodesAvoidedSafely() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "XX,IN,YYY,21dksaj,AUS",
            onCountryClickListener = mock()
        )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun duplicateCodesAreRemoved() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "IN,AUS,IN",
            onCountryClickListener = mock()
        )

        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
        assertEquals(2, helper.allPreferredCountries.size)
    }

    @Test
    fun duplicateCountriesAreRemoved() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            preferredCountryCodes = "IN,AUS,IND",
            onCountryClickListener = mock()
        )

        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
        assertEquals(2, helper.allPreferredCountries.size)
    }

    @Test
    fun `filter for name`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("United States")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for part of name`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("United")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for english name`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("United States")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for part of english name`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("nited")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for alpha3 code`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            cpCountryRowConfig = CPCountryRowConfig(highlightedTextGenerator = { it.alpha3 }),
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("LKA")
        assertEquals("LK", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for alpha2 code`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            cpCountryRowConfig = CPCountryRowConfig(highlightedTextGenerator = { it.alpha2 }),
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("LK")
        assertEquals("LK", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for phone code`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            cpCountryRowConfig = CPCountryRowConfig(highlightedTextGenerator = { it.phoneCode.toString() }),
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("94")
        assertEquals("LK", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for partial phone code`() {
        val helper = CPRecyclerViewHelper(
            cpDataStore = dataStore,
            cpCountryRowConfig = CPCountryRowConfig(highlightedTextGenerator = { it.phoneCode.toString() }),
            onCountryClickListener = mock()
        )
        helper.updateDataForQuery("9")

        assertTrue(helper.controllerData.allCountries.any { it.alpha2 == "AF" })
        assertTrue(helper.controllerData.allCountries.any { it.alpha2 == "IN" })
        assertTrue(helper.controllerData.allCountries.any { it.alpha2 == "LK" })
        assertEquals(3, helper.controllerData.allCountries.size)
    }

}