package com.example.rainy.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.rainy.R

//val nunitoFamily = FontFamily(
//    Font(R.font.nunito_light, FontWeight.Light),
//    Font(R.font.nunito_regular, FontWeight.Normal),
//    Font(R.font.nunito_italic, FontWeight.Normal, FontStyle.Italic),
//    Font(R.font.nunito_medium, FontWeight.Medium),
//    Font(R.font.nunito_bold, FontWeight.Bold),
//)

val fontName = GoogleFont("Poppins")

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontFam = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

@Preview
@Composable
fun FontTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Whereas", fontFamily = fontFam, fontWeight = FontWeight.Light, fontSize = 32.sp)
        Text(text = "Whereas", fontFamily = fontFam, fontWeight = FontWeight.Normal, fontSize = 32.sp)
        Text(text = "Whereas", fontFamily = fontFam, fontWeight = FontWeight.Medium, fontSize = 32.sp)
        Text(text = "Whereas", fontFamily = fontFam, fontWeight = FontWeight.Bold, fontSize = 32.sp)
    }

}

