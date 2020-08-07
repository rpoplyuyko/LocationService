package com.example.locationservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessageReceiver(main: MainActivity) : BroadcastReceiver() {
    var mainActivity: MainActivity = main
    override fun onReceive(context: Context, intent: Intent) {
        mainActivity.update()
    }
}
