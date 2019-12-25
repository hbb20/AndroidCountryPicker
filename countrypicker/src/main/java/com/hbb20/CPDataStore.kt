package com.hbb20

data class CPDataStore(
    val cpLanguage: CPLanguage,
    val countryList: List<CPCountry>,
    val dialogTitle: String,
    val searchHint: String,
    val noResultAck: String,
    val emptySelectionText: String
)