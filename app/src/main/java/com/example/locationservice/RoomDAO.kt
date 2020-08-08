package com.example.locationservice

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDAO{
    @Query("SELECT * FROM item_table ORDER BY item_date DESC")
    fun allItems(): LiveData<List<Item>>

    @Query("INSERT INTO item_table (item_address, item_latitude, item_longtitude, item_date, age) VALUES (:address, :lat, :lon, :dt, datetime('now'))")
    fun addItem(address: String, lat: String, lon: String, dt: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Query("DELETE FROM item_table WHERE age <= datetime('now', '-1 minutes')")
    fun deleteQuery()

    @Query("DELETE FROM item_table")
    fun deleteAll()
}