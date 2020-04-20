package com.madlab.locationupdatesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLocation.setOnClickListener {
            /* LocationHelper().startUserLocation(
           applicationContext,
           object : LocationHelper.MyLocationListener {
               override fun onLocationUpdate(location: Location?) {
                   Toast.makeText(
                       applicationContext,
                       "lat:- ${location?.latitude} long:- ${location?.longitude}",
                       Toast.LENGTH_LONG
                   ).show()
               }

           }, this
       )*/

            /*MyLocationHelper.getUserLocation(
            applicationContext,
            object : MyLocationHelper.MyLocationListener {
                override fun onLocationUpdate(location: Location?) {
                    Toast.makeText(
                        applicationContext,
                        "lat:- ${location?.latitude} long:- ${location?.longitude}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            },
            this
        )*/
        }

    }
}