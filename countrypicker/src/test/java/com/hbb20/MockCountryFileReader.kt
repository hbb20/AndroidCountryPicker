package com.hbb20

import android.content.Context
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.models.CPDataStore

object MockCountryFileReader :
        CountryFileReading {

    override fun readMasterDataFromFiles(context: Context): CPDataStore {
        return getSampleDataStore()
    }
}

