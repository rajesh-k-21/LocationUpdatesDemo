package com.madlab.locationupdatesdemo.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.madlab.locationupdatesdemo.MainActivity

class LocationHelper {

    var myLocationListener: MyLocationListener? = null

    companion object {
        const val LOCATION_UPDATE_TIME = 5000
        const val MIN_DISTRACT = 50
    }

    interface MyLocationListener {
        fun onLocationUpdate(location: Location?)
    }

    @SuppressLint("MissingPermission")
    fun startUserLocation(
        context: Context,
        myListener: MyLocationListener, mainActivity: MainActivity
    ) {
        myLocationListener = myListener

        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                myLocationListener?.onLocationUpdate(location)
                Toast.makeText(
                    context,
                    "Location Update !",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Toast.makeText(
                    context,
                    "provider:- $provider \n status:- $status ",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(
                    context,
                    "provider:- $provider \n  Provider is Enable",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onProviderDisabled(provider: String?) {
                Toast.makeText(
                    context,
                    "provider:- $provider \n  Provider is Disabled",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        when {
            checkPermissionForLocation(context) -> {
                when {
                    checkGpsEnable(context) -> {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            LOCATION_UPDATE_TIME.toLong(),
                            MIN_DISTRACT.toFloat(),
                            locationListener
                        )
                    }
                    else -> {
                        Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else -> {
                requestLocationPermission(mainActivity)
            }
        }
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
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestLocationPermission(activity: MainActivity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 99
        )
    }
}