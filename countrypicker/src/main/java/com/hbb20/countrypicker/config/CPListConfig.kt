package com.hbb20.countrypicker.config

data class CPListConfig(
    var preferredCountryCodes: String? = defaultCPListPreferredCountryCodes,
) {
    companion object {
        val defaultCPListPreferredCountryCodes: String? = null
    }
}
