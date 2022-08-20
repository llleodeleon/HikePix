package com.leodeleon.hikepix.service

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.leodeleon.hikepix.domain.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LocationManager(
    private val client: FusedLocationProviderClient
) {
    companion object {
        private const val RADIUS_DISTANCE = 100
    }

    private val _locationFlow = MutableSharedFlow<LatLng>(replay = 1)
    val locationFlow: SharedFlow<LatLng> = _locationFlow

    private var currentLocation: Location? = null
    private lateinit var callback: LocationCallback

    private val locationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(10)
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    fun requestLocation() {
        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return

                val emitLocation = currentLocation?.let {
                    val distance = it.distanceTo(location)
                    Timber.d("distance: $distance")
                    distance >= RADIUS_DISTANCE
                } ?: true

                if (emitLocation) {
                    currentLocation = location
                    val latLng = LatLng(location.latitude, location.longitude)
                    _locationFlow.tryEmit(latLng)
                }
            }
        }

        try {
            client.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper()
            )
        } catch (t: Throwable) {
            Timber.e(t)
        }
    }

    fun dispose() {
        client.removeLocationUpdates(callback)
    }
}