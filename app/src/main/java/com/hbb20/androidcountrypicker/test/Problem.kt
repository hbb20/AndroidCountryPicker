package com.hbb20.androidcountrypicker.test

import com.hbb20.countrypicker.loge
import com.hbb20.countrypicker.logw

class Problem(
    val category: ProblemCategory,
    val fileName: String,
    val solution: String
) {
    override fun toString(): String {
        return "$category :: $fileName ::=> $solution"
    }

    fun log(){
        if(category == ProblemCategory.UNVERIFIED_ENTRIES){
            logw(toString())
        }else{
            loge(toString())
        }
    }
}



enum class ProblemCategory(val text: String) {
    INVALID_VALUE("Invalid value"),
    MISSING_PROPERTY("Missing Property"),
    MISSING_FILE("Missing File"),
    DUPLICATE_ENTRY("Duplicate Entry"),
    INVALID_ORDER("Invalid order"),
    EXTRA_ENTRIES("Extra entries"),
    UNVERIFIED_ENTRIES("Unverified Translations")
}


