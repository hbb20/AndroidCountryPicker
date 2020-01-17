package com.hbb20

import android.content.Context
import com.hbb20.countrypicker.CPRecyclerViewHelper
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CPRecyclerViewHelperTest {

    private val context = mock<Context> {}


    @Test
    fun preferredCountriesWorkWithAlpha2() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val preferredCountries =
            CPRecyclerViewHelper.extractPreferredCountries(dataStore.countryList, "IN,AU")
        assertEquals("IN", preferredCountries[0].alpha2Code)
        assertEquals("AU", preferredCountries[1].alpha2Code)
    }

    @Test
    fun trimCodeBeforePreferred() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val preferredCountries =
            CPRecyclerViewHelper.extractPreferredCountries(dataStore.countryList, "IN ,AU")
        assertEquals("IN", preferredCountries[0].alpha2Code)
        assertEquals("AU", preferredCountries[1].alpha2Code)
    }

    @Test
    fun preferredCountriesWorkWithAlpha3() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val preferredCountries =
            CPRecyclerViewHelper.extractPreferredCountries(dataStore.countryList, "IND,AUS")
        assertEquals("IN", preferredCountries[0].alpha2Code)
        assertEquals("AU", preferredCountries[1].alpha2Code)
    }

    @Test
    fun preferredCountriesWorkWithAlpha2and3() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val preferredCountries =
            CPRecyclerViewHelper.extractPreferredCountries(dataStore.countryList, "IN,AUS")
        assertEquals("IN", preferredCountries[0].alpha2Code)
        assertEquals("AU", preferredCountries[1].alpha2Code)
    }

    @Test
    fun invalidCodesAvoidedSafely() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val preferredCountries = CPRecyclerViewHelper.extractPreferredCountries(
            dataStore.countryList,
            "XX,IN,YYY,21dksaj,AUS"
        )
        assertEquals("IN", preferredCountries[0].alpha2Code)
        assertEquals("AU", preferredCountries[1].alpha2Code)
    }

    @Test
    fun duplicateCodesAreRemoved() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val preferredCountries =
            CPRecyclerViewHelper.extractPreferredCountries(dataStore.countryList, "IN,AUS,IN")
        assertEquals("IN", preferredCountries[0].alpha2Code)
        assertEquals("AU", preferredCountries[1].alpha2Code)
        assertEquals(2, preferredCountries.size)
    }

    @Test
    fun duplicateCountriesAreRemoved() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val preferredCountries =
            CPRecyclerViewHelper.extractPreferredCountries(dataStore.countryList, "IN,AUS,IND")
        assertEquals("IN", preferredCountries[0].alpha2Code)
        assertEquals("AU", preferredCountries[1].alpha2Code)
        assertEquals(2, preferredCountries.size)
    }

    @Test
    fun `filter for name`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries =
            CPRecyclerViewHelper.filterCountries(dataStore.countryList, "United States")
        assertEquals("US", filteredCountries[0].alpha2Code)
        assertEquals(1, filteredCountries.size)
    }

    @Test
    fun `filter for part of name`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries =
            CPRecyclerViewHelper.filterCountries(dataStore.countryList, "United")
        assertEquals("US", filteredCountries[0].alpha2Code)
        assertEquals(1, filteredCountries.size)
    }

    @Test
    fun `filter for english name`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries =
            CPRecyclerViewHelper.filterCountries(dataStore.countryList, "United States (USA)")
        assertEquals("US", filteredCountries[0].alpha2Code)
        assertEquals(1, filteredCountries.size)
    }

    @Test
    fun `filter for part of english name`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries =
            CPRecyclerViewHelper.filterCountries(dataStore.countryList, "(usa)")
        assertEquals("US", filteredCountries[0].alpha2Code)
        assertEquals(1, filteredCountries.size)
    }

    @Test
    fun `filter for alpha3 code`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries = CPRecyclerViewHelper.filterCountries(dataStore.countryList, "LKA")
        assertEquals("LK", filteredCountries[0].alpha2Code)
        assertEquals(1, filteredCountries.size)
    }

    @Test
    fun `filter for alpha2 code`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries = CPRecyclerViewHelper.filterCountries(dataStore.countryList, "LK")
        assertEquals("LK", filteredCountries[0].alpha2Code)
        assertEquals(1, filteredCountries.size)
    }

    @Test
    fun `filter for phone code`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries = CPRecyclerViewHelper.filterCountries(dataStore.countryList, "94")
        assertEquals("LK", filteredCountries[0].alpha2Code)
        assertEquals(1, filteredCountries.size)
    }

    @Test
    fun `filter for partial phone code`() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        val filteredCountries = CPRecyclerViewHelper.filterCountries(dataStore.countryList, "9")
        assertTrue(filteredCountries.any { it.alpha2Code == "AF" })
        assertTrue(filteredCountries.any { it.alpha2Code == "IN" })
        assertTrue(filteredCountries.any { it.alpha2Code == "LK" })
        assertEquals(3, filteredCountries.size)
    }

}