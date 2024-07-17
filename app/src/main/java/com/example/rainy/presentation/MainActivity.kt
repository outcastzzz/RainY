package com.example.rainy.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import com.arkivanov.decompose.defaultComponentContext
import com.example.common.theme.RainYTheme
import com.example.rainy.RainyApp
import com.example.rainy.presentation.root.RootComponentImpl
import com.example.rainy.presentation.root.RootContent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val permissionsArray = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory

    private val component by lazy {
        (applicationContext as RainyApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("api_30", "working")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            SetupAppStart()
            Log.d("api_30", "setContent too")
        }
    }

    @Composable
    private fun SetupAppStart() {
        val latitudeState = remember { mutableStateOf<Float?>(null) }
        val longitudeState = remember { mutableStateOf<Float?>(null) }

        Log.d("api_30", "setupApp too")

        LaunchedEffect(Unit) {
            Log.d("api_30", "setContent too2")
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("api_30", "setContent too3")
                if (isLocationEnabled()) {
                    Log.d("api_30", "setContent too4")
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        )
                    )
                }
                return@LaunchedEffect
            } else {
                requestPermissions()
            }

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        Log.d("mytag", "Latitude: ${it.latitude}, Longitude: ${it.longitude}")
                        latitudeState.value = it.latitude.toFloat()
                        longitudeState.value = it.longitude.toFloat()
                    }
                }
        }

        RainYTheme {
            val lat = latitudeState.value
            val long = longitudeState.value

            if (lat != null && long != null) {
                RootContent(
                    component = rootComponentFactory.create(
                        defaultComponentContext(),
                        lat = lat,
                        long = long
                    )
                )
            }
        }

    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled()) {
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
                return
            }
        } else {
            requestPermissions()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            Log.d("api_30", "req loc")
            getLocation()
        } else {
            Log.d("mytag", "Location permission denied")
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            permissionsArray,
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    companion object {
        private const val PERMISSION_ID = 1000
    }

}