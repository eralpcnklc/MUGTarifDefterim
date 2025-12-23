package com.example.yemektarifiuygulamasi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Renkleri Color.kt dosyasından çekiyoruz
private val LightColorScheme = lightColorScheme(
    primary = TerraCotta,
    secondary = SageGreen,
    tertiary = Sand,
    background = Eggshell,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = Gunmetal,
    onSurface = Gunmetal,
)

private val DarkColorScheme = darkColorScheme(
    primary = TerraCotta,
    secondary = SageGreen,
    tertiary = Sand,
    background = Gunmetal,
    onBackground = Eggshell
)

@Composable
fun YemekTarifiUygulamasiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Dinamik rengi kapattık
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}