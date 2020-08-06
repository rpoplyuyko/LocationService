package com.example.locationservice

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteQuery
import android.widget.Toast
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import java.sql.SQLOutput

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)

abstract class RoomSingleton : RoomDatabase(){
    abstract fun roomDAO():RoomDAO

    companion object : SingletonHolder<RoomSingleton, Context>({
        Room.databaseBuilder(it.applicationContext,
            RoomSingleton::class.java, "Sample.db")
            .build()
    })

}

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}