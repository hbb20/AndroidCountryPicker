package com.hbb20.countrypicker.flagprovider

import androidx.annotation.DrawableRes
import java.util.Locale

abstract class CPFlagProvider

/**
 * If you wish to useEmojiCompat = true then make sure to enable emoji-compat in your app using
 * [Downloadable font configuration](https://developer.android.com/guide/topics/ui/look-and-feel/emoji-compat#downloadable-fonts)
 * or [Bundled Font Configuration](https://developer.android.com/guide/topics/ui/look-and-feel/emoji-compat#bundled-fonts)
 */
class DefaultEmojiFlagProvider(val useEmojiCompat: Boolean = false) : CPFlagProvider()

/**
 * [alpha2ToFlag] is map of alpha 2 code to drawable res of flag
 * [missingFlagPlaceHolder] is shown for country for which flag is not found in [alpha2ToFlag].
 * Ideally [missingFlagPlaceHolder] should be transparent image and it's size should match size of
 * other flags in this pack to maintain visual symmetry.
 */
class CPFlagImageProvider(
    alpha2ToFlag: Map<String, Int>,
    @DrawableRes val missingFlagPlaceHolder: Int
) : CPFlagProvider() {
    private val flagMap = alpha2ToFlag.map { it.key.uppercase(Locale.ENGLISH) to it.value }.toMap()

    @DrawableRes
    fun getFlag(alpha2Code: String): Int {
        val upperCaseAlpha2Code = alpha2Code.uppercase(Locale.ENGLISH)
        val flag = flagMap[upperCaseAlpha2Code]
        return flag ?: flagMap.getOrElse(
            getNormalizedAlpha2ForFlag(upperCaseAlpha2Code)
        ) { missingFlagPlaceHolder }
    }

    /**
     * Some entities (like islands) uses flag of parent entity.
     * For example, "United States Minor Outlying Islands" (UM) uses flag of "United states" (US)
     * This function will convert "UM" to "US"
     */
    private fun getNormalizedAlpha2ForFlag(alpha2Code: String): String {
        return when (alpha2Code.uppercase(Locale.ENGLISH)) {
            "UM" -> "US"
            "SJ" -> "NO"
            "BV" -> "NO"
            "HM" -> "AU"
            else -> alpha2Code.uppercase(Locale.ENGLISH)
        }
    }
}
