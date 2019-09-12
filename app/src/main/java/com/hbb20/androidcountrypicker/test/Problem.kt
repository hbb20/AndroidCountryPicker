package com.hbb20.androidcountrypicker.test

class Problem(
    val title: ProblemTitle,
    val desc: String? = null,
    val fileName: String,
    val level: ProblemLevel = ProblemLevel.ERROR,
    val solution: String
) {

}

enum class ProblemLevel {
    ERROR,
    WARNING
}


enum class ProblemTitle(text: String) {
    INVALID_VALUE("Invalid key for property"),
    MISSING_PROPERTY("Missing Property"),
    MISSING_FILE("Missing File"),
    DUPLICATE_ENTRY("Duplicate Entry"),
    INVALID_ORDER("Invalid order"),
    EXTRA_ENTRIES("Extra entries"),
    UNVERIFIED_ENTRIES("Unverified Translations")
}


