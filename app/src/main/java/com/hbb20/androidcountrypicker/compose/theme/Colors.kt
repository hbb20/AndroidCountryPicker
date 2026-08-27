package com.hbb20.androidcountrypicker.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) darkThemeColors else lightThemeColors,
        content = content,
    )
}

val lightThemeColors =
    lightColors(
        primary = Color(0xFF008577),
        primaryVariant = Color(0xFF00574B),
        secondary = Color(0xFF3F51B5),
        secondaryVariant = Color(0xFF3F51B5),
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFFFFFFFF),
        error = Color(0xFFED5933),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFFF4F4F7),
        onBackground = Color(0xFF000000),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF000000),
    )

val darkThemeColors =
    darkColors(
        primary = Color(0xFF00E0C9),
        primaryVariant = Color(0xFF00C7AB),
        secondary = Color(0xFF3F51B5),
        secondaryVariant = Color(0xFF3F51B5),
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFFFFFFFF),
        error = Color(0xFFED5933),
        onError = Color(0xFFFFFFFF),
        background = Color(0xFF000000),
        onBackground = Color(0xFFFAFAFC),
        surface = Color(0xFF292B2E),
        onSurface = Color(0xFFFAFAFC),
    )
