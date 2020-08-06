package com.example.locationservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.locationservice.LocationService.Companion.initDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mDb: RoomSingleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the service status
        buttonStart.setOnClickListener{
            startService(Intent(applicationContext, LocationService::class.java))
            initDatabase(applicationContext)
        }

        // Button to stop the service
        buttonStop.setOnClickListener{
            stopService(Intent(applicationContext, LocationService::class.java))
            //  mDb = RoomSingleton.getInstance(applicationContext)
        }
    }
}