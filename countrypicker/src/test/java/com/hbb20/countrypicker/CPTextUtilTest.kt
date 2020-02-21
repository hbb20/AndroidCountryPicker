package com.hbb20.countrypicker

import com.hbb20.getSampleDataStore
import org.junit.Assert
import org.junit.Test

class CPTextUtilTest {

    @Test
    fun `NAME template is replaced`() {
        val country = getSampleDataStore().countryList.find {
            it.alpha2 == "IN"
        }!!
        val displayText = CPTextUtil.prepare("(${CPInfoUnit.NAME})", country)
        Assert.assertEquals("(India)", displayText)
    }

    @Test
    fun `ALPHA2 template is replaced`() {
        val country = getSampleDataStore().countryList.find {
            it.alpha2 == "IN"
        }!!
        val displayText = CPTextUtil.prepare(
            "${CPInfoUnit.NAME} - (${CPInfoUnit.ALPHA2})",
            country
        )
        Assert.assertEquals("India - (IN)", displayText)
    }

    @Test
    fun `ALPHA3 template is replaced`() {
        val country = getSampleDataStore().countryList.find {
            it.alpha2 == "IN"
        }!!
        val displayText = CPTextUtil.prepare(
            "${CPInfoUnit.NAME} - (${CPInfoUnit.ALPHA3})",
            country
        )
        Assert.assertEquals("India - (IND)", displayText)
    }

    @Test
    fun `PhoneCode template is replaced`() {
        val country = getSampleDataStore().countryList.find {
            it.alpha2 == "IN"
        }!!
        val displayText = CPTextUtil.prepare(
            "${CPInfoUnit.NAME} (+${CPInfoUnit.PHONECODE})",
            country
        )
        Assert.assertEquals("India (+91)", displayText)
    }

    @Test
    fun `EnglishName template is replaced`() {
        val country = getSampleDataStore().countryList.find {
            it.alpha2 == "IN"
        }!!.copy(englishName = "TestingEnglishName")
        val displayText = CPTextUtil.prepare("'${CPInfoUnit.ENGLISHNAME}'", country)
        Assert.assertEquals("'TestingEnglishName'", displayText)
    }
}