package com.example.locationservice

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)

abstract class RoomSingleton : RoomDatabase(){
    abstract fun roomDAO():RoomDAO

    companion object {
        @Volatile private var instance: RoomSingleton? = null
        fun getInstance(context: Context): RoomSingleton {
            if (instance == null) {
                instance = Room.databaseBuilder(context, RoomSingleton::class.java, "Sample.db").build()
                return instance as RoomSingleton
            }
            return instance as RoomSingleton
        }
    }

}