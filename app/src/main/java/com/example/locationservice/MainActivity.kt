package com.example.locationservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button to stop the service
        buttonStop.setOnClickListener{
            stopService(Intent(applicationContext, LocationService::class.java))
        }

        // Get the service status
        buttonStart.setOnClickListener{
            startService(Intent(applicationContext, LocationService::class.java))
        }
    }
}