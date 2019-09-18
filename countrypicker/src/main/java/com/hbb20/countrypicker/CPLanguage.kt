package com.hbb20.countrypicker

import java.util.*

enum class CPLanguage(
    val code: String,
    val country: String? = null,
    val script: String? = null
) {
    AFRIKAANS("af"),
    ARABIC("ar"),
    BENGALI("bn"),
    CHINESE_SIMPLIFIED("zh", "CN", "Hans"),
    CHINESE_TRADITIONAL("zh", "TW", "Hant"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    FARSI("fa"),
    FRENCH("fr"),
    GERMAN("de"),
    GREEK("el"),
    GUJARATI("gu"),
    HEBREW("iw"),
    HINDI("hi"),
    INDONESIA("in"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    PUNJABI("pa"),
    RUSSIAN("ru"),
    SLOVAK("sk"),
    SPANISH("es"),
    SWEDISH("sv"),
    TURKISH("tr"),
    UKRAINIAN("uk"),
    URDU("ur"),
    UZBEK("uz"),
    VIETNAMESE("vi");

    val translationFileName: String by lazy { "cp_${name.toLowerCase(Locale.ROOT)}.xml" }
}