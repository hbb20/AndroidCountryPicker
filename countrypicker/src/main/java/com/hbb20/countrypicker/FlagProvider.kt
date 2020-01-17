package com.hbb20.countrypicker

sealed class FlagProvider
class DefaultEmojiFlagProvider(val useEmojiCompat: Boolean = false) : FlagProvider()
abstract class CustomFlagImageProvider : FlagProvider() {
    abstract fun getFlagResIdForAlphaCode(alpha2Code: String): Int
}
