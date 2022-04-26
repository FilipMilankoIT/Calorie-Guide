package com.example.calorieguide.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val orange100 = Color(0xFFFFCC80)
val orange200 = Color(0xFFFF7539)
val orange400 = Color(0xFFFF3d00)
val orange700 = Color(0xFFC30000)
val red500 = Color(0xFFF05545)
val red900 = Color(0xFFB71C1C)
val amber200 = Color(0xFFFFFD61)
val amber400 = Color(0xFFFFCA28)
val amber600 = Color(0xFFFBC02D)
val amber700 = Color(0xFFC79A00)
val teal_200 = Color(0xFF03DAC5)
val teal_700 = Color(0xFF018786)
val gray200 = Color(0xFFF0F0F0)
val gray300 = Color(0xFFDCDCDC)
val gray400 = Color(0xFFC0C0C0)
val gray800 = Color(0xFF464646)
val gray900 = Color(0xFF181818)
val white_alpha25 = Color(0x40FFFFFF)
val blue = Color(0xFF2196F3)

val LightColors = lightColors(
    orange400,
    orange700,
    amber400,
    amber700,
    onPrimary = Color.White,
    onSecondary = Color.White
)

val DarkColors = darkColors(
    orange200,
    orange400,
    amber200,
    amber400,
    gray900,
    gray900,
    onPrimary = Color.White,
    onSecondary = Color.White
)

val Colors.formIconDefault: Color
    get() = if (isLight) gray800 else Color.White

val Colors.formIconDisabled: Color
    get() = if (isLight) gray300 else white_alpha25