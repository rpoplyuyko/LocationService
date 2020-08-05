package com.example.locationservice

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var locationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var locationService: LocationService? = null

    private val locationServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
            locationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
            locationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceClass = LocationService::class.java
        val serviceIntent = Intent(applicationContext, serviceClass)

        buttonStart.setOnClickListener() {
            if (foregroundPermissionApproved()) {
                val serviceIntent = Intent(this, LocationService::class.java)
                bindService(serviceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE)
            } else {
                requestForegroundPermissions()
            }
        }
        buttonStop.setOnClickListener() {
            if (locationServiceBound) {
                unbindService(locationServiceConnection)
                locationServiceBound = false
            }
        }
    }

    // Permission request ↓↓↓↓↓↓↓↓↓
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }
}
