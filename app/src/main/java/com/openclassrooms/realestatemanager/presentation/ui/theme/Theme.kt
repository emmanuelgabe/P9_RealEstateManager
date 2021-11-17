package com.openclassrooms.realestatemanager.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = LightBlue300,
    primaryVariant = Blue400,
    secondary = LightBlue300Dark,
    background = Color.White,
)

@Composable
fun RealEstateManagerComposeTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}