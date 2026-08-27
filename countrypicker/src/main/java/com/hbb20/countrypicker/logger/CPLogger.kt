package com.hbb20.countrypicker.logger

import android.util.Log
import com.hbb20.countrypicker.models.CP_TAG

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
