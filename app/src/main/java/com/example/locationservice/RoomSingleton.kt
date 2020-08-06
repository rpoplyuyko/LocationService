package com.example.locationservice

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)
abstract class RoomSingleton : RoomDatabase(){
    abstract fun roomDAO():RoomDAO

    companion object{
        private var INSTANCE: RoomSingleton? = null
        @SuppressLint("ShowToast")
        fun getInstance(context: Context): RoomSingleton{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    RoomSingleton::class.java,
                    "roomdb")
                    .build()
                Toast.makeText(context,"Start Database", Toast.LENGTH_SHORT)
            }

            return INSTANCE as RoomSingleton
        }
    }
}