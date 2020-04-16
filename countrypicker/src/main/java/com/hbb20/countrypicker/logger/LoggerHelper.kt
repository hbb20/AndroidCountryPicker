package com.hbb20.countrypicker.logger

import timber.log.Timber

internal const val LOG_TAG = "CountryPicker"

val methodStartTimeMap = mutableMapOf<String, Long>()

fun onMethodBegin(methodName: String) {
    methodStartTimeMap[methodName] = System.currentTimeMillis()
}

fun logMethodEnd(methodName: String) {
    methodStartTimeMap[methodName]?.let {
        val current = System.currentTimeMillis()
        Timber.tag("CP Method Time").d("$methodName : ${(current - it) / 1000.00} sec")
        methodStartTimeMap.remove(methodName)
    }
}