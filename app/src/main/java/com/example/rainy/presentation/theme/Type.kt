package com.example.rainy.presentation.theme

import android.os.Build
import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.sp
import com.example.rainy.R

val default = FontFamily(
    Font(
        R.font.nunito_regular
    )
)

@OptIn(ExperimentalTextApi::class)
val displayFontFamily = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    FontFamily(
        Font(
            R.font.nunito_variable,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(600),
                FontVariation.width(20f),
                FontVariation.slant(2f),
            )
        )
    )
} else {
    default
}

val AppTypo = Typography(
    displaySmall = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 12.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 18.sp
    ),
    displayLarge = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 10.sp
    )
)