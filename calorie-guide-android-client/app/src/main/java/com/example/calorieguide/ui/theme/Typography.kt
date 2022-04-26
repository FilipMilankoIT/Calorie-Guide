package com.example.calorieguide.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.calorieguide.R

val PoppinsRegular = FontFamily(Font(R.font.poppins_regular))

val PoppinsMedium = FontFamily(Font(R.font.poppins_medium))

val PoppinsSemiBold = FontFamily(Font(R.font.poppins_semi_bold))

val MyTypography = Typography(
    PoppinsMedium,
    h6 = TextStyle(
        fontFamily = PoppinsMedium,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = PoppinsMedium,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = PoppinsMedium,
        fontSize = 14.sp
    )
)

val textField: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = PoppinsMedium,
        fontSize = 16.sp,
        color = MaterialTheme.colors.onSurface
    )