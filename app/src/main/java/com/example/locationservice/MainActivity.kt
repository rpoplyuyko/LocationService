package com.example.locationservice

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity() {
    var messageReceiver: BroadcastReceiver? = MessageReceiver(this)
    val KEY_BROADCAST = "MessageUpdateDB"

    private lateinit var instance: RoomSingleton
    private lateinit var listItem: List<Item>
    val adapter = ItemListAdapter()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        instance = RoomSingleton.getInstance(applicationContext)

        update()

        // Button to start the service
        buttonStart.setOnClickListener {
            if (foregroundPermissionApproved()) {
                val filter = IntentFilter(KEY_BROADCAST)
                registerReceiver(messageReceiver, filter)
                startService(Intent(applicationContext, LocationService::class.java))
            } else {
                requestForegroundPermissions()
            }
        }

        // Button to stop the service
        buttonStop.setOnClickListener{
            unregisterReceiver(messageReceiver)
            stopService(Intent(applicationContext, LocationService::class.java))
        }
    }

    fun update() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        listItem = emptyList<Item>()
        doAsync {
            listItem = instance.roomDAO().allItems()
            adapter.setItems(listItem)
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