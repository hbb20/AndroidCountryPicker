package com.hbb20.countrypicker.config


data class CPRecyclerViewConfig(
    val preferredCountryCodes: String? = defaultPreferredCountryCodes
) {
    companion object {
        val defaultPreferredCountryCodes: String? = null
    }
}
