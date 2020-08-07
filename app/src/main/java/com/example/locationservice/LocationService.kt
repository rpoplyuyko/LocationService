package com.example.locationservice

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import org.jetbrains.anko.doAsync
import java.util.concurrent.TimeUnit


class LocationService : Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    val KEY_BROADCAST = "MessageUpdateDB"
    private lateinit var instance: RoomSingleton

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification(getTextLocation(currentLocation)))
        objectCallback()
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper())
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        instance = RoomSingleton.getInstance(applicationContext)

        ////////////////////////////////////////////
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(8)
            maxWaitTime = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    fun objectCallback() {
        locationCallback = object : LocationCallback() {
            @TargetApi(Build.VERSION_CODES.O)
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0?.lastLocation != null) {
                    currentLocation = p0.lastLocation
                    showNotification()

                    val item = Item(
                        getAddress(applicationContext, p0.lastLocation),
                        getCoordinates(p0.lastLocation, true),
                        getCoordinates(p0.lastLocation, false),
                        getDateStr())

                    doAsync {
                        instance.roomDAO().insert(item)
                    }
                } else {
                    showNotification()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopSelf()
    }

    private fun createNotification(showText: String) : Notification {
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(showText)
            .setBigContentTitle(titleText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(showText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun showNotification() {
        notificationManager.notify(
            NOTIFICATION_ID,
            createNotification(getTextLocation(currentLocation))
        )

    }

    private fun getTextLocation(location: Location?) : String {
        if (location != null) {
            val lat = location.latitude.toDouble()
            val lon = location.longitude.toDouble()
            return "$lat°, $lon°"
        } else {
            return "Location is empty"
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 12345678
        private const val NOTIFICATION_CHANNEL_ID = "locaion_service_channel_01"
        private const val titleText = "Location Service"

        fun start(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            context.stopService(intent)
        }
    }
}