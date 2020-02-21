package com.hbb20

import android.content.res.Resources

object MockCountryFileReader : CountryFileReading {

    override fun readMasterDataFromFiles(resources: Resources): CPDataStore {
        return getSampleDataStore()
    }
}

