package com.example.locationservice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val adapter = ItemListAdapter()
    private lateinit var recyclerView: RecyclerView

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        itemViewModel.allWords.observe(this, Observer { items ->
            // Update the cached copy of the words in the adapter.
            items?.let { adapter.setItems(it) }
        })

        // Button to start the service
        buttonStart.setOnClickListener {
            requestForegroundPermissions()
            if (foregroundPermissionApproved()) {
                if (checkGpsStatus(this)) {
                    startService(Intent(applicationContext, LocationService::class.java))
                } else {
                    Toast.makeText(this, "GPS disabled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Button to stop the service
        buttonStop.setOnClickListener{
            stopService(Intent(applicationContext, LocationService::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        selectLayoutManager()
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

    private fun selectLayoutManager() {
        if (checkOrientation()) {
            recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
            recyclerView.addItemDecoration(GridItemDecoration(10, 1))
        } else {
            recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
            recyclerView.addItemDecoration(GridItemDecoration(10, 2))
        }
    }

    fun checkOrientation() : Boolean {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        return true
    }
}