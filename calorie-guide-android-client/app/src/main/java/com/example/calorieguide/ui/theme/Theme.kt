package com.example.calorieguide.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        if (darkTheme) DarkColors else LightColors,
        MyTypography,
        MaterialTheme.shapes,
        content
    )
}