package com.example.locationservice

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private lateinit var notificationManager: NotificationManager
    private var currentLocation: Location? = null
    private lateinit var locationCallback: LocationCallback

    private val localBinder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper())
        return localBinder
    }

    inner class LocalBinder : Binder() {
        internal val service: LocationService
            get() = this@LocationService
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUnbind(intent: Intent?): Boolean {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
        stopForeground(true)
        stopService(intent)
        return true
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(8)
            maxWaitTime = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            @TargetApi(Build.VERSION_CODES.O)
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0?.lastLocation != null) {
                    currentLocation = p0.lastLocation
                    showNotification()
                } else {
                    showNotification()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateNotification(showText: String): Notification {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Location Service", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(showText)
            .setBigContentTitle("Location Service")

        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle("Location Service")
            .setContentText(showText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        notificationManager.notify(
            NOTIFICATION_ID,
            generateNotification(getTextLocation(currentLocation)))
    }

    private fun getTextLocation(location: Location?) : String {
        if (location != null) {
            val lat = location.latitude.toDouble()
            val lon = location.longitude.toDouble()
            return "$lat,$lon"
        } else {
            return "Location is empty"
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 12345678

        private const val NOTIFICATION_CHANNEL_ID = "location_service_channel_01"
    }
}