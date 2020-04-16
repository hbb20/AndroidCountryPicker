package com.hbb20

import android.content.res.Resources
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.models.CPDataStore

object MockCountryFileReader :
        CountryFileReading {

    override fun readMasterDataFromFiles(resources: Resources): CPDataStore {
        return getSampleDataStore()
    }
}

