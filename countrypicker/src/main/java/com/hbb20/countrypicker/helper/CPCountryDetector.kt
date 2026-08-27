package com.hbb20.countrypicker.helper

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager

class CPCountryDetector(val context: Context) {
    fun detectCountry(
        sources: List<Source> =
            listOf(
                Source.SIM,
                Source.NETWORK,
                Source.LOCALE,
            ),
    ): String? {
        sources.forEach {
            val detectedCountry =
                when (it) {
                    Source.SIM -> detectSIMCountry()
                    Source.NETWORK -> detectNetworkCountry()
                    Source.LOCALE -> detectLocaleCountry()
                }
            if (detectedCountry != null) return detectedCountry
        }
        return null
    }

    /**
     * This will detect country from SIM info and then returns alpha2.
     * @return alpha2 code of country if detected else null
     */
    fun detectSIMCountry(): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.simCountryIso
    }

    /**
     * This will detect country from NETWORK info.
     *
     * @return alpha2 code of country if detected else null
     */
    fun detectNetworkCountry(): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso
    }

    /**
     * This will detect country from LOCALE info.
     *
     * @return alpha2 code of country if detected else null
     */
    fun detectLocaleCountry(): String? {
        val config = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0].country
        } else {
            config.locale.country
        }
    }

    sealed class Source(val name: String) {
        object SIM : Source("Sim")

        object NETWORK : Source("Network")

        object LOCALE : Source("Locale")
    }
}
