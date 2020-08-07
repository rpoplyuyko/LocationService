package com.example.locationservice

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity() {
    var messageReceiver: BroadcastReceiver? = MessageReceiver()
    val KEY_BROADCAST = "MessageUpdateDB"
    private lateinit var instance: RoomSingleton
    lateinit var listItem: List<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val adapter = ItemListAdapter(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        listItem = emptyList<Item>()
        instance = RoomSingleton.getInstance(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            listItem = instance.roomDAO().allItems()
            adapter.setItems(listItem)
        }

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

        Toast.makeText(applicationContext, listItem.size.toString(),Toast.LENGTH_SHORT).show()
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
}