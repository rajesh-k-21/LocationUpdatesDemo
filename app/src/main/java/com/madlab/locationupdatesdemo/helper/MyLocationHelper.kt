package com.madlab.locationupdatesdemo.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.madlab.locationupdatesdemo.MainActivity

object MyLocationHelper {

    var myLocationListener: MyLocationListener? = null
    val PERMISSION_ID: Int = 99

    interface MyLocationListener {
        fun onLocationUpdate(location: Location?)
    }

    fun getUserLocation(
        context: Context,
        myLocationListener: MyLocationListener,
        activity: MainActivity
    ) {
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

        MyLocationHelper.myLocationListener = myLocationListener

        if (checkPermissionForLocation(context)) {
            if (checkGpsEnable(context)) {

                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData(myLocationListener, activity)
                    } else {
                        Toast.makeText(context, "Location Update !", Toast.LENGTH_SHORT).show()
                        myLocationListener.onLocationUpdate(location)
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
            }
        } else {
            requestLocationPermission(activity)
        }
    }

    private fun requestNewLocationData(myListener: MyLocationListener, activity: MainActivity) {
        myLocationListener = myListener
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.numUpdates = 1

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                p0?.let {
                    Toast.makeText(
                        activity.applicationContext,
                        "Location CallBack",
                        Toast.LENGTH_SHORT
                    ).show()
                    myLocationListener?.onLocationUpdate(it.lastLocation)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkGpsEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 99
        )
    }
}