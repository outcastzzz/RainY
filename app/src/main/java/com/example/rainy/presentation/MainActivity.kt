package com.example.rainy.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.arkivanov.decompose.defaultComponentContext
import com.example.rainy.RainyApp
import com.example.rainy.presentation.root.RootComponentImpl
import com.example.rainy.presentation.root.RootContent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val permissionsArray = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory

    private val scope = CoroutineScope(Dispatchers.IO)

    private val latitudeState = mutableStateOf<Float?>(null)
    private val longitudeState = mutableStateOf<Float?>(null)

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
        }
    }

    @Composable
    private fun SetupAppStart() {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            tryToGetLocation(context)
        }

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

    private fun tryToGetLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
            return
        }

        scope.launch {
            requestCurrentLocation()
        }
    }

    private suspend fun requestCurrentLocation() {
        withContext(Dispatchers.IO) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val locationRequest =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                        .setWaitForAccurateLocation(false)
                        .setMinUpdateIntervalMillis(5000)
                        .setMaxUpdateDelayMillis(10000)
                        .build()

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                latitudeState.value = location.latitude.toFloat()
                longitudeState.value = location.longitude.toFloat()
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (isLocationEnabled()) {
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                scope.launch {
                    tryToGetLocation(this@MainActivity)
                }
            } else {
                requestPermissions()
            }
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