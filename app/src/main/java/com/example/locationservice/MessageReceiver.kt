package com.example.locationservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.locationservice.LocationService.Companion.getListItems

class MessageReceiver : BroadcastReceiver() {
    private lateinit var list: List<Item>

    override fun onReceive(context: Context, intent: Intent) {

    }
}
