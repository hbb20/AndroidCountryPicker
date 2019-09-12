package com.hbb20.countrypicker

import android.util.Log

internal const val LOG_TAG = "CountryPicker"

fun logd(debugMessage: String) {
    Log.d(LOG_TAG, debugMessage)
}

fun loge(errorMessage: String) {
    Log.e(LOG_TAG, errorMessage)
}