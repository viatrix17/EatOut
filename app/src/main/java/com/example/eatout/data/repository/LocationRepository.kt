package com.example.eatout.data.repository
import com.google.android.gms.location.Priority
import kotlin.coroutines.resume

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine

import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationRepository(private val fusedLocationClient: FusedLocationProviderClient) {

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (locationCallback != null) {
            Log.d("DEBUG_LOCATION", "Śledzenie już jest aktywne.")
            return
        }
        Log.d("DEBUG_LOCATION", "Uruchamiam nasłuchiwanie lokalizacji...")
        // Jeśli już nasłuchujemy, nie rób tego drugi raz
        if (locationCallback != null) return

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // Pobieramy ostatnią lokalizację z wyniku
                locationResult.lastLocation?.let { loc ->
                    _location.value = loc
                    Log.d("DEBUG_LOCATION", "Otrzymano nową lokalizację: Lat: ${loc.latitude}, Lon: ${loc.longitude}")
                } ?: Log.w("DEBUG_LOCATION", "Otrzymano wynik lokalizacji, ale ostatnia lokalizacja jest null.")
            }
        }

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback!!,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            Log.e("DEBUG_LOCATION", "Błąd podczas żądania lokalizacji: ${e.message}")
        }
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
            locationCallback = null
        }
    }
}