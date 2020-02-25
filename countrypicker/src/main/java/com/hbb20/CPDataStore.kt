package com.hbb20

data class CPDataStore(
    var countryList: MutableList<CPCountry>,
    val messageGroup: MessageGroup
) {
    data class MessageGroup(
        var noMatchMsg: String = "No matching result found",
        var searchHint: String = "Search...",
        var dialogTitle: String = "Select a country",
        var selectionPlaceholderText: String = "Country"
    )
}