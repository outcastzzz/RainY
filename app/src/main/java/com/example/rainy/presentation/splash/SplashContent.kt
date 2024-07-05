package com.example.rainy.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.rainy.R
import com.example.domain.entity.InfoData

@Composable
fun SplashContent(component: SplashComponent) {

    val state by component.model.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        when(val splashState = state.splashState) {
            SplashStore.State.SplashState.Error -> SplashError()
            SplashStore.State.SplashState.Initial -> {}
            SplashStore.State.SplashState.Loading -> SplashLoading()
            is SplashStore.State.SplashState.SuccessLoaded -> SplashStateLoaded(splashState.data.cityName) {
                component.dataLoaded(
                    com.example.domain.entity.InfoData(
                        splashState.data.cityName,
                        splashState.data.forecast,
                        splashState.data.weather,
                        splashState.data.astronomy
                    )
                )
            }
        }
    }

}

@Composable
private fun SplashStateLoaded(
    cityName: String,
    onDataLoaded: (String) -> Unit,
) {
    LaunchedEffect("loaded") {
        onDataLoaded(cityName)
    }
}

@Composable
private fun SplashLoading() {

    val anim = if(isSystemInDarkTheme()) R.raw.dark_mode_anim else R.raw.light_mode_anim

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(anim)
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop,
            outlineMasksAndMattes = true,
            iterations = LottieConstants.IterateForever,
        )
    }

}

@Composable
private fun SplashError() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Проверьте подключение к интернету",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
        )
    }
}