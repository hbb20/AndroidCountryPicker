package com.hbb20

import android.util.Log

internal const val LOG_TAG = "CountryPicker"

fun logd(debugMessage: String) {
    Log.d(LOG_TAG, debugMessage)
}

fun logw(debugMessage: String) {
    Log.w(LOG_TAG, debugMessage)
}

fun loge(errorMessage: String) {
    Log.e(LOG_TAG, errorMessage)
}