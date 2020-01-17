package com.hbb20.countrypicker

import com.hbb20.CPLanguage
import com.hbb20.getSampleDataStore
import org.junit.Assert
import org.junit.Test

class CPTextUtilTest {

    @Test
    fun `NAME template is replaced`() {
        val country = getSampleDataStore(cpLanguage = CPLanguage.ENGLISH).countryList.find {
            it.alpha2Code == "IN"
        }!!
        val displayText = CPTextUtil.prepare("(${CPInfoUnit.NAME.template})", country)
        Assert.assertEquals("(India)", displayText)
    }

    @Test
    fun `ALPHA2 template is replaced`() {
        val country = getSampleDataStore(cpLanguage = CPLanguage.ENGLISH).countryList.find {
            it.alpha2Code == "IN"
        }!!
        val displayText = CPTextUtil.prepare(
            "${CPInfoUnit.NAME.template} - (${CPInfoUnit.ALPHA2.template})",
            country
        )
        Assert.assertEquals("India - (IN)", displayText)
    }

    @Test
    fun `ALPHA3 template is replaced`() {
        val country = getSampleDataStore(cpLanguage = CPLanguage.ENGLISH).countryList.find {
            it.alpha2Code == "IN"
        }!!
        val displayText = CPTextUtil.prepare(
            "${CPInfoUnit.NAME.template} - (${CPInfoUnit.ALPHA3.template})",
            country
        )
        Assert.assertEquals("India - (IND)", displayText)
    }

    @Test
    fun `PhoneCode template is replaced`() {
        val country = getSampleDataStore(cpLanguage = CPLanguage.ENGLISH).countryList.find {
            it.alpha2Code == "IN"
        }!!
        val displayText = CPTextUtil.prepare(
            "${CPInfoUnit.NAME.template} (+${CPInfoUnit.PHONECODE.template})",
            country
        )
        Assert.assertEquals("India (+91)", displayText)
    }

    @Test
    fun `EnglishName template is replaced`() {
        val country = getSampleDataStore(cpLanguage = CPLanguage.ENGLISH).countryList.find {
            it.alpha2Code == "IN"
        }!!.copy(englishName = "TestingEnglishName")
        val displayText = CPTextUtil.prepare("'${CPInfoUnit.ENGLISHNAME.template}'", country)
        Assert.assertEquals("'TestingEnglishName'", displayText)
    }
}