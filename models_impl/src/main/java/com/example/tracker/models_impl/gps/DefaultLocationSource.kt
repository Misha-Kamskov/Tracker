package com.example.tracker.models_impl.gps

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import com.example.models_impl.BuildConfig
import com.example.tracker.models.gps.LocationSource
import com.example.tracker.models.locations.Location
import com.example.tracker.utils.PermissionsUtil
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationSource(
    private val context: Context,
) : LocationSource {

    private val gpsStatusFlow = MutableStateFlow(false)

    @SuppressLint("MissingPermission")
    override fun observeLocations(): Flow<Location> {
        return callbackFlow {

            if (!PermissionsUtil.hasLocationPermission(context)) {
                throw LocationSource.LocationException("Missing location permission")
            }

            val locationManager: LocationManager = context.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager

            gpsStatusFlow.tryEmit(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: android.location.Location) {
                    val mark = Location(
                        time = location.time.toString(),
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    launch { send(mark) }
                }

                override fun onProviderDisabled(provider: String) {
                    gpsStatusFlow.tryEmit(false)
                }

                override fun onProviderEnabled(provider: String) {
                    gpsStatusFlow.tryEmit(true)
                }
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, BuildConfig.PERIOD, BuildConfig.SENSITIVITY,
                locationListener, Looper.getMainLooper()
            )

            awaitClose {
                locationManager.removeUpdates(locationListener)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun getGpsStatusFlow(): Flow<Boolean> = gpsStatusFlow

}
