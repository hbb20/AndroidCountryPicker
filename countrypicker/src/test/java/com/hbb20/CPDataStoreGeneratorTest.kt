package com.hbb20

import android.content.res.Resources
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

class CPDataStoreGeneratorTest {

    private val resources = mock<Resources> {}


    @Test
    fun generate() {
        val dataStore =
            CPDataStoreGenerator.generate(resources, countryFileReader = MockCountryFileReader)
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `only custom master country alpha2 is returned`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "AU,TG,ZA",
                countryFileReader = MockCountryFileReader
            )

        assertTrue(dataStore.countryList.any { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "ZA" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `only custom master country alpha3 is returned`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.any { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "AFG" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `only custom master country (alpha3 and alpha2 mix) is returned`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "AU,TG,ZA,LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.any { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "AFG" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "ZA" })
        assertEquals(6, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha3 is ignored for custom master list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3 == "QQQ" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "AFG" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 is ignored for custom master list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "AU,TG,QQ,ZA",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2 == "QQ" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "ZA" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 and alpha3 mix is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "AU,TG,DD,ZA,LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2 == "DD" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "QQQ" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "AFG" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "ZA" })
        assertEquals(6, dataStore.countryList.size)
    }

    @Test
    fun `blank custom master list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `invalid custom master list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "QQ,DDD,ANYThing",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }


    @Test
    fun `custom excluded countries alpha2`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "AU,TG,ZA",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "ZA" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `custom excluded countries alpha3`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "AFG" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `custom excluded countries alpha2 and alpha3 mix`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "AU,TG,ZA,LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "AFG" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "ZA" })
        assertEquals(4, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha3 is ignored for custom excluded list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "AFG" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 is ignored for custom excluded list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "AU,TG,QQ,ZA",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "ZA" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 and alpha3 mix is ignored for custom excluded list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "AU,TG,DD,ZA,LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "AFG" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "ZA" })
        assertEquals(4, dataStore.countryList.size)
    }

    @Test
    fun `blank custom excluded list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "  ",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `invalid custom excluded list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customExcludedCountries = "QQ,DDD,ANYThing",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `custom master list and excluded list does not crash`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "AU,TG,DD,ZA,LKA,GHA,AFG,QQQ",
                customExcludedCountries = "AUS,ZA,IN,QQ,DDD,ANYThing",
                countryFileReader = MockCountryFileReader
            )
        //master list: AU/AUS,TG,ZA,LKA,GHA,AFG
        //after excluded: TG,LKA,GHA,AFG
        assertEquals(4, dataStore.countryList.size)
    }

    @Test
    fun `ignore excluded list if it matches same set as custom master list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                customMasterCountries = "AU,TG,DD,ZA,LKA,GHA,AFG",
                customExcludedCountries = "AUS,TG,AU,ZA,LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        //master list: AU,TG,ZA,LKA,GHA,AFG
        assertEquals(6, dataStore.countryList.size)
    }

    @Test
    fun `check duplicate file reading is not done for the same language`() {
        val fileReader = mock<CountryFileReading> {}
        whenever(fileReader.readMasterDataFromFiles(any())).thenReturn(
            getSampleDataStore()
        )
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                countryFileReader = fileReader
            )
        verify(fileReader, Mockito.times(1)).readMasterDataFromFiles(any())
        val dataStore2 =
            CPDataStoreGenerator.generate(
                resources,
                countryFileReader = fileReader
            )
        verify(fileReader, Mockito.times(1)).readMasterDataFromFiles(any())
    }

    @Test
    fun `force read file using useCache = false`() {
        val fileReader = mock<CountryFileReading> {}
        CPDataStoreGenerator.invalidateCache()
        whenever(fileReader.readMasterDataFromFiles(any())).thenReturn(
            getSampleDataStore()
        )
        val dataStore =
            CPDataStoreGenerator.generate(
                resources,
                countryFileReader = fileReader
            )
        verify(fileReader, Mockito.times(1)).readMasterDataFromFiles(any())
        val dataStore2 =
            CPDataStoreGenerator.generate(
                resources = resources,
                countryFileReader = fileReader,
                useCache = false
            )
        verify(fileReader, Mockito.times(2)).readMasterDataFromFiles(any())
    }
}