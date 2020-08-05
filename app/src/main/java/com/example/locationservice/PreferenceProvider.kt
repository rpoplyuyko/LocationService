package com.example.locationservice

import android.content.Context

class PreferenceProvider(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("locationPreferences", 0)

    // Boolean
    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String) : Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    // String
    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String) : String? {
        return sharedPreferences.getString(key, null)
    }

    // Integer
    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String) : Int {
        return sharedPreferences.getInt(key, 0)
    }
}