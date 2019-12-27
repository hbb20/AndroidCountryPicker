package com.hbb20

import android.content.Context
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

class CPDataStoreGeneratorTest {

    private val context = mock<Context> {}


    @Test
    fun generate() {
        val dataStore =
            CPDataStoreGenerator.generate(context, countryFileReader = MockCountryFileReader)
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `only custom master country alpha2 is returned`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "AU,TG,ZA",
                countryFileReader = MockCountryFileReader
            )

        assertTrue(dataStore.countryList.any { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "ZA" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `only custom master country alpha3 is returned`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.any { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "AFG" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `only custom master country (alpha3 and alpha2 mix) is returned`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "AU,TG,ZA,LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.any { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "AFG" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "ZA" })
        assertEquals(6, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha3 is ignored for custom master list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3Code == "QQQ" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "AFG" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 is ignored for custom master list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "AU,TG,QQ,ZA",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2Code == "QQ" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "ZA" })
        assertEquals(3, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 and alpha3 mix is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "AU,TG,DD,ZA,LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2Code == "DD" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "QQQ" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3Code == "AFG" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2Code == "ZA" })
        assertEquals(6, dataStore.countryList.size)
    }

    @Test
    fun `blank custom master list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `invalid custom master list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customMasterCountries = "QQ,DDD,ANYThing",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }


    @Test
    fun `custom excluded countries alpha2`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "AU,TG,ZA",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "ZA" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `custom excluded countries alpha3`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "AFG" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `custom excluded countries alpha2 and alpha3 mix`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "AU,TG,ZA,LKA,GHA,AFG",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "AFG" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "ZA" })
        assertEquals(4, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha3 is ignored for custom excluded list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "AFG" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 is ignored for custom excluded list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "AU,TG,QQ,ZA",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "ZA" })
        assertEquals(7, dataStore.countryList.size)
    }

    @Test
    fun `invalid alpha2 and alpha3 mix is ignored for custom excluded list`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "AU,TG,DD,ZA,LKA,GHA,AFG,QQQ",
                countryFileReader = MockCountryFileReader
            )
        assertTrue(dataStore.countryList.none { it.alpha3Code == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3Code == "AFG" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2Code == "ZA" })
        assertEquals(4, dataStore.countryList.size)
    }

    @Test
    fun `blank custom excluded list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "  ",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `invalid custom excluded list is ignored`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                customExcludedCountries = "QQ,DDD,ANYThing",
                countryFileReader = MockCountryFileReader
            )
        assertEquals(10, dataStore.countryList.size)
    }

    @Test
    fun `custom master list and excluded list does not crash`() {
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
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
                context,
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
        whenever(fileReader.loadDataStoreFromXML(any(), any())).thenReturn(
            getSampleDataStore(
                CPLanguage.ENGLISH
            )
        )
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                defaultLanguage = CPLanguage.ENGLISH,
                countryFileReader = fileReader
            )
        verify(fileReader, Mockito.times(1)).loadDataStoreFromXML(any(), any())
        val dataStore2 =
            CPDataStoreGenerator.generate(
                context,
                defaultLanguage = CPLanguage.ENGLISH,
                countryFileReader = fileReader
            )
        verify(fileReader, Mockito.times(1)).loadDataStoreFromXML(any(), any())
    }

    @Test
    fun `check correct requested file is read`() {
        val fileReader = mock<CountryFileReading> {}
        whenever(fileReader.loadDataStoreFromXML(any(), any())).thenReturn(
            getSampleDataStore(
                CPLanguage.ENGLISH
            )
        )
        val dataStore =
            CPDataStoreGenerator.generate(
                context,
                defaultLanguage = CPLanguage.JAPANESE,
                countryFileReader = fileReader
            )
        verify(fileReader, Mockito.times(1)).loadDataStoreFromXML(any(), eq(CPLanguage.JAPANESE))
        verify(fileReader, Mockito.times(0)).loadDataStoreFromXML(any(), eq(CPLanguage.ENGLISH))
    }

    @Test
    fun `file read take place if language changes`() {
        val fileReader = mock<CountryFileReading> {}
        whenever(fileReader.loadDataStoreFromXML(any(), eq(CPLanguage.HINDI))).thenReturn(
            getSampleDataStore(CPLanguage.HINDI)
        )
        whenever(fileReader.loadDataStoreFromXML(any(), eq(CPLanguage.URDU))).thenReturn(
            getSampleDataStore(CPLanguage.URDU)
        )
        var dataStore =
            CPDataStoreGenerator.generate(
                context,
                defaultLanguage = CPLanguage.HINDI,
                countryFileReader = fileReader
            )
        assertEquals(CPLanguage.HINDI, dataStore.cpLanguage)
        verify(fileReader, Mockito.times(1)).loadDataStoreFromXML(any(), eq(CPLanguage.HINDI))
        verify(fileReader, Mockito.times(0)).loadDataStoreFromXML(any(), eq(CPLanguage.URDU))
        dataStore =
            CPDataStoreGenerator.generate(
                context,
                defaultLanguage = CPLanguage.URDU,
                countryFileReader = fileReader
            )
        assertEquals(CPLanguage.URDU, dataStore.cpLanguage)
        verify(fileReader, Mockito.times(1)).loadDataStoreFromXML(any(), eq(CPLanguage.HINDI))
        verify(fileReader, Mockito.times(1)).loadDataStoreFromXML(any(), eq(CPLanguage.URDU))
        dataStore =
            CPDataStoreGenerator.generate(
                context,
                defaultLanguage = CPLanguage.HINDI,
                countryFileReader = fileReader
            )
        assertEquals(CPLanguage.HINDI, dataStore.cpLanguage)
        verify(fileReader, Mockito.times(2)).loadDataStoreFromXML(any(), eq(CPLanguage.HINDI))
        verify(fileReader, Mockito.times(1)).loadDataStoreFromXML(any(), eq(CPLanguage.URDU))
    }

}