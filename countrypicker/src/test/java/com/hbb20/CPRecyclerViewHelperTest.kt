package com.hbb20

import android.content.Context
import com.hbb20.countrypicker.config.CPListConfig
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.recyclerview.CPRecyclerViewHelper
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CPRecyclerViewHelperTest {
    private val context = mockk<Context> {}
    private val dataStore =
        CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)

    @Test
    fun preferredCountriesWorkWithAlpha2() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpListConfig = CPListConfig(preferredCountryCodes = "IN,AU"),
                onCountryClickListener = mockk(),
            )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun trimCodeBeforePreferred() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpListConfig = CPListConfig(preferredCountryCodes = "IN ,AU"),
                onCountryClickListener = mockk(),
            )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun preferredCountriesWorkWithAlpha3() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpListConfig = CPListConfig(preferredCountryCodes = "IND,AUS"),
                onCountryClickListener = mockk(),
            )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun preferredCountriesWorkWithAlpha2and3() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpListConfig = CPListConfig(preferredCountryCodes = "IN,AUS"),
                onCountryClickListener = mockk(),
            )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun invalidCodesAvoidedSafely() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpListConfig = CPListConfig(preferredCountryCodes = "XX,IN,YYY,21dksaj,AUS"),
                onCountryClickListener = mockk(),
            )
        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
    }

    @Test
    fun duplicateCodesAreRemoved() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpListConfig = CPListConfig(preferredCountryCodes = "IN,AUS,IN"),
                onCountryClickListener = mockk(),
            )

        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
        assertEquals(2, helper.allPreferredCountries.size)
    }

    @Test
    fun duplicateCountriesAreRemoved() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpListConfig = CPListConfig(preferredCountryCodes = "IN,AUS,IND"),
                onCountryClickListener = mockk(),
            )

        assertEquals("IN", helper.allPreferredCountries[0].alpha2)
        assertEquals("AU", helper.allPreferredCountries[1].alpha2)
        assertEquals(2, helper.allPreferredCountries.size)
    }

    @Test
    fun `filter for name`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("United States")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for part of name`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("United")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for english name`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("United States")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for part of english name`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("nited")

        assertEquals("US", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for alpha3 code`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpRowConfig = CPRowConfig(highlightedTextGenerator = { it.alpha3 }),
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("LKA")
        assertEquals("LK", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for alpha2 code`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpRowConfig = CPRowConfig(highlightedTextGenerator = { it.alpha2 }),
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("LK")
        assertEquals("LK", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for phone code`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpRowConfig = CPRowConfig(highlightedTextGenerator = { it.phoneCode.toString() }),
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("94")
        assertEquals("LK", helper.controllerData.allCountries[0].alpha2)
        assertEquals(1, helper.controllerData.allCountries.size)
    }

    @Test
    fun `filter for partial phone code`() {
        val helper =
            CPRecyclerViewHelper(
                cpDataStore = dataStore,
                cpRowConfig = CPRowConfig(highlightedTextGenerator = { it.phoneCode.toString() }),
                onCountryClickListener = mockk(),
            )
        helper.updateDataForQuery("9")

        assertTrue(helper.controllerData.allCountries.any { it.alpha2 == "AF" })
        assertTrue(helper.controllerData.allCountries.any { it.alpha2 == "IN" })
        assertTrue(helper.controllerData.allCountries.any { it.alpha2 == "LK" })
        assertEquals(3, helper.controllerData.allCountries.size)
    }
}
