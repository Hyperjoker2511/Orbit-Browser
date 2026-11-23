package com.orbit.browser

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Orbit theme — Material 3 colour schemes built from Zephium's tokens
 * (see [OrbitTokens]). Dark is the base; light is the mirror.
 * The rail, the address field and the start page all read from these
 * schemes so the whole app stays one design.
 */
private val OrbitDarkScheme = darkColorScheme(
    primary = Color(OrbitTokens.DARK_TEXT),
    onPrimary = Color(OrbitTokens.DARK_CANVAS),
    background = Color(OrbitTokens.DARK_CANVAS),
    onBackground = Color(OrbitTokens.DARK_TEXT),
    surface = Color(OrbitTokens.DARK_CHROME),
    onSurface = Color(OrbitTokens.DARK_TEXT),
    surfaceVariant = Color(OrbitTokens.DARK_SURFACE),
    onSurfaceVariant = Color(OrbitTokens.DARK_LABEL_SECONDARY),
    surfaceContainer = Color(OrbitTokens.DARK_SURFACE),
    surfaceContainerHigh = Color(OrbitTokens.DARK_RAISED),
    outline = Color(OrbitTokens.DARK_BORDER),
    outlineVariant = Color(OrbitTokens.DARK_BORDER_STRONG),
)

private val OrbitLightScheme = lightColorScheme(
    primary = Color(OrbitTokens.LIGHT_TEXT),
    onPrimary = Color(OrbitTokens.LIGHT_SURFACE),
    background = Color(OrbitTokens.LIGHT_CANVAS),
    onBackground = Color(OrbitTokens.LIGHT_TEXT),
    surface = Color(OrbitTokens.LIGHT_CHROME),
    onSurface = Color(OrbitTokens.LIGHT_TEXT),
    surfaceVariant = Color(OrbitTokens.LIGHT_SURFACE),
    onSurfaceVariant = Color(OrbitTokens.LIGHT_LABEL_SECONDARY),
    surfaceContainer = Color(OrbitTokens.LIGHT_SURFACE),
    surfaceContainerHigh = Color(OrbitTokens.LIGHT_RAISED),
    outline = Color(OrbitTokens.LIGHT_BORDER),
    outlineVariant = Color(OrbitTokens.LIGHT_BORDER_STRONG),
)

@Composable
fun OrbitTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) OrbitDarkScheme else OrbitLightScheme,
        content = content,
    )
}

/** The rail / chrome colour for the current theme (Zephium's `--color-chrome`). */
@Composable
fun orbitChromeColor(darkTheme: Boolean): Color =
    Color(if (darkTheme) OrbitTokens.DARK_CHROME else OrbitTokens.LIGHT_CHROME)

/** The page colour for the current theme (Zephium's `--color-page`). */
@Composable
fun orbitPageColor(darkTheme: Boolean): Color =
    Color(if (darkTheme) OrbitTokens.DARK_PAGE else OrbitTokens.LIGHT_PAGE)

/** The active-row fill for the current theme (Zephium's `--row-active`). */
@Composable
fun orbitRowActiveColor(darkTheme: Boolean): Color =
    Color(if (darkTheme) OrbitTokens.DARK_ROW_ACTIVE else OrbitTokens.LIGHT_ROW_ACTIVE)

/** The address-field fill for the current theme (Zephium's `--color-address`). */
@Composable
fun orbitAddressColor(darkTheme: Boolean): Color =
    Color(if (darkTheme) OrbitTokens.DARK_ADDRESS else OrbitTokens.LIGHT_ADDRESS)
