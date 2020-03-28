package com.hbb20.countrypicker

sealed class FlagProvider
/**
 * If you wish to useEmojiCompat = true then make sure to enable emoji-compat in your app using
 * [Downloadable font configuration](https://developer.android.com/guide/topics/ui/look-and-feel/emoji-compat#downloadable-fonts)
 * or [Bundled Font Configuration](https://developer.android.com/guide/topics/ui/look-and-feel/emoji-compat#bundled-fonts)
 */
class DefaultEmojiFlagProvider(val useEmojiCompat: Boolean = false) : FlagProvider()

abstract class CustomFlagImageProvider : FlagProvider() {
    abstract fun getFlag(alpha2Code: String): Int
}
