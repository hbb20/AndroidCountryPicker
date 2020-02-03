package com.hbb20

data class CPDataStore(
    val countryList: MutableList<CPCountry>,
    val messageCollection: MessageCollection
) {
    data class MessageCollection(
        var noMatchMsg: String = "No matching result found",
        var searchHint: String = "Search...",
        var dialogTitle: String = "Select a country",
        var selectionPlaceholderText: String = "Country"
    )
}