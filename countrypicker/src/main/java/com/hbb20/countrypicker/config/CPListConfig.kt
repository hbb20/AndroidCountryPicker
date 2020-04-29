package com.hbb20.countrypicker.config


data class CPListConfig(
    var preferredCountryCodes: String? = defaultPreferredCountryCodes
) {
    companion object {
        val defaultPreferredCountryCodes: String? = null
    }
}
