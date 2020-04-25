package com.hbb20.countrypicker.config


data class CPRecyclerViewConfig(
    var preferredCountryCodes: String? = defaultPreferredCountryCodes
) {
    companion object {
        val defaultPreferredCountryCodes: String? = null
    }
}
