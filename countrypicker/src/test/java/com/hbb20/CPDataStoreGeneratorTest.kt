package com.hbb20

import androidx.test.platform.app.InstrumentationRegistry
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CPDataStoreGeneratorTest {

    private val context = InstrumentationRegistry.getInstrumentation().context

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

        assertTrue(dataStore.countryList.any { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.any { it.alpha2 == "ZA" })
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
        assertTrue(dataStore.countryList.any { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.any { it.alpha3 == "AFG" })
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
                context,
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
                context,
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
                context,
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
        assertTrue(dataStore.countryList.none { it.alpha2 == "AU" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "TG" })
        assertTrue(dataStore.countryList.none { it.alpha2 == "ZA" })
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
        assertTrue(dataStore.countryList.none { it.alpha3 == "LKA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "GHA" })
        assertTrue(dataStore.countryList.none { it.alpha3 == "AFG" })
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
                context,
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
                context,
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
                context,
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
        // master list: AU/AUS,TG,ZA,LKA,GHA,AFG
        // after excluded: TG,LKA,GHA,AFG
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
        // master list: AU,TG,ZA,LKA,GHA,AFG
        assertEquals(6, dataStore.countryList.size)
    }
}
