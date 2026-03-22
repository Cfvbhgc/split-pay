package com.splitpay.app.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Surface,
    primaryContainer = Green80,
    secondary = Teal40,
    secondaryContainer = Teal80,
    tertiary = Orange40,
    tertiaryContainer = Orange80,
    surface = Surface
)

private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = GreenDark,
    primaryContainer = Green40,
    secondary = Teal80,
    secondaryContainer = Teal40,
    tertiary = Orange80,
    tertiaryContainer = Orange40,
    surface = SurfaceDark
)

@Composable
fun SplitPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
