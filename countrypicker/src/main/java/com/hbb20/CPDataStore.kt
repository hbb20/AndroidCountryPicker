package com.hbb20

data class CPDataStore(
    val cpLanguage: CPLanguage,
    val masterCountryList: List<CPCountry>,
    val dialogTitle: String,
    val searchHint: String,
    val noResultAck: String
)