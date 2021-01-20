class MessageGroup(
    val noMatchMsg: String,
    val searchHint: String,
    val dialogTitle: String,
    val selectionPlaceHolder: String,
    val clearSelection: String
) {
    fun xmlMessages(): List<Pair<String, String>> {
        return listOf(
            "cp_no_match_msg" to noMatchMsg,
            "cp_search_hint" to searchHint,
            "cp_dialog_title" to dialogTitle,
            "cp_selection_placeholder" to selectionPlaceHolder,
            "cp_clear_selection" to clearSelection
        )
    }
}

