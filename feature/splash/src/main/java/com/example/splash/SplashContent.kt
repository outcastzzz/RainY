package com.example.splash

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.common.R
import com.example.domain.entity.InfoData

@Composable
fun SplashContent(modifier: Modifier, component: SplashComponent) {

    val state by component.model.collectAsStateWithLifecycle()

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        when(val splashState = state.splashState) {
            SplashStore.State.SplashState.Error -> SplashError()
            SplashStore.State.SplashState.Initial -> {}
            SplashStore.State.SplashState.Loading -> SplashLoading()
            is SplashStore.State.SplashState.SuccessLoaded -> SplashStateLoaded(splashState.data.cityName) {
                component.dataLoaded(
                    InfoData(
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
private fun SplashLoading(modifier: Modifier = Modifier) {

    val anim = if(isSystemInDarkTheme()) R.raw.dark_mode_anim else R.raw.light_mode_anim

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(anim)
    )

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieAnimation(
            composition = composition,
            modifier = modifier.size(200.dp),
            contentScale = ContentScale.Crop,
            outlineMasksAndMattes = true,
            iterations = LottieConstants.IterateForever,
        )
    }

}

@Composable
private fun SplashError(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.checkout_network_connect),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(start = 30.dp, end = 30.dp)
        )
    }
}