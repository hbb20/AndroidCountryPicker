package com.hbb20

import android.util.Log

internal const val LOG_TAG = "CountryPicker"

val methodStartTimeMap = mutableMapOf<String, Long>()

fun logd(debugMessage: String) {
    Log.d(LOG_TAG, debugMessage)
}

fun logw(debugMessage: String) {
    Log.w(LOG_TAG, debugMessage)
}

fun loge(errorMessage: String) {
    Log.e(LOG_TAG, errorMessage)
}

fun onMethodBegin(methodName: String) {
    methodStartTimeMap[methodName] = System.currentTimeMillis()
}

fun logMethodEnd(methodName: String) {
    methodStartTimeMap[methodName]?.let {
        val current = System.currentTimeMillis()
        Log.d("CP Method Time", "$methodName : ${(current - it) / 1000.00} sec")
        methodStartTimeMap.remove(methodName)
    }
}