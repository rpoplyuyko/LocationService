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

//open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
//    private var creator: ((A) -> T)? = creator
//    @Volatile private var instance: T? = null
//
//    fun getInstance(arg: A): T {
//        val i = instance
//        if (i != null) {
//            return i
//        }
//
//        return synchronized(this) {
//            val i2 = instance
//            if (i2 != null) {
//                i2
//            } else {
//                val created = creator!!(arg)
//                instance = created
//                creator = null
//                created
//            }
//        }
//    }
//}