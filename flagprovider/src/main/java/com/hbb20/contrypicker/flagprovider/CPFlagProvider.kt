package com.hbb20.contrypicker.flagprovider

import androidx.annotation.DrawableRes

sealed class CPFlagProvider

/**
 * If you wish to useEmojiCompat = true then make sure to enable emoji-compat in your app using
 * [Downloadable font configuration](https://developer.android.com/guide/topics/ui/look-and-feel/emoji-compat#downloadable-fonts)
 * or [Bundled Font Configuration](https://developer.android.com/guide/topics/ui/look-and-feel/emoji-compat#bundled-fonts)
 */
class DefaultEmojiFlagProvider(val useEmojiCompat: Boolean = false) : CPFlagProvider()

abstract class CPFlagImageProvider : CPFlagProvider() {

    abstract val alpha2ToFlag: Map<String, Int>
    abstract val missingFlagPlaceHolder: Int

    @DrawableRes
    fun getFlag(alpha2Code: String): Int {
        return alpha2ToFlag.getOrElse(
            getNormalizedAlpha2ForFlag(alpha2Code),
            { missingFlagPlaceHolder })
    }

    /**
     * Some entities (like islands) uses flag of parent entity.
     * For example, "United States Minor Outlying Islands" (UM) uses flag of "United states" (US)
     * This function will convert "UM" to "US"
     */
    private fun getNormalizedAlpha2ForFlag(alpha2Code: String): String {
        return when (alpha2Code.toLowerCase()) {
            "um" -> "us"
            "sj" -> "no"
            "bv" -> "no"
            "hm" -> "au"
            else -> alpha2Code.toLowerCase()
        }
    }
}
