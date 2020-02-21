package com.hbb20

import android.util.Log

object CPLogger {

    fun d(message: String) {
        Log.d(CP_TAG, message)
    }

    fun e(message: String) {
        Log.e(CP_TAG, message)
    }

    fun i(message: String) {
        Log.i(CP_TAG, message)
    }

    fun v(message: String) {
        Log.v(CP_TAG, message)
    }
}