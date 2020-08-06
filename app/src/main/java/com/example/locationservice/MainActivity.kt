package com.example.locationservice

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mDb: RoomSingleton
    var messageReceiver: BroadcastReceiver? = MessageReceiver()
    val KEY_BROADCAST = "MessageUpdateDB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the service status
        buttonStart.setOnClickListener {
            if (foregroundPermissionApproved()) {
                startService(Intent(applicationContext, LocationService::class.java))
            } else {
                requestForegroundPermissions()
            }
        }

        // Button to stop the service
        buttonStop.setOnClickListener{
            stopService(Intent(applicationContext, LocationService::class.java))
            //  mDb = RoomSingleton.getInstance(applicationContext)
        }
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter(KEY_BROADCAST)
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(messageReceiver, filter);
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(messageReceiver);
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

    fun getContext() : Context {
        return applicationContext
    }
}