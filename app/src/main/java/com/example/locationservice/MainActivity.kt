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
    private lateinit var preferenceProvider: PreferenceProvider
    private var locationService: LocationService? = null

    private val locationServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
            preferenceProvider.putBoolean(Constants.KEY_SERVICE_BOUND, true)
            preferenceProvider.putBoolean(Constants.KEY_RUNNING_SERVICE, true)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
            preferenceProvider.putBoolean(Constants.KEY_SERVICE_BOUND, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceProvider = PreferenceProvider(applicationContext)

        if (preferenceProvider.getBoolean(Constants.KEY_RUNNING_SERVICE)) {
            val serviceIntent = Intent(applicationContext, LocationService::class.java)
            bindService(serviceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE)
        }

        buttonStart.setOnClickListener() {
            if (foregroundPermissionApproved() && !preferenceProvider.getBoolean(Constants.KEY_RUNNING_SERVICE)) {
                val serviceIntent = Intent(applicationContext, LocationService::class.java)
                bindService(serviceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE)
            } else {
                requestForegroundPermissions()
            }
        }
        buttonStop.setOnClickListener() {
            if (preferenceProvider.getBoolean(Constants.KEY_SERVICE_BOUND)) {
                unbindService(locationServiceConnection)
                preferenceProvider.putBoolean(Constants.KEY_RUNNING_SERVICE, false)
                preferenceProvider.putBoolean(Constants.KEY_SERVICE_BOUND, false)
            }
        }
    }

    override fun onDestroy() {
        preferenceProvider.putBoolean(Constants.KEY_SERVICE_BOUND, false)
        super.onDestroy()
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
