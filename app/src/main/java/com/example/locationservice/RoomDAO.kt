package com.example.locationservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDAO{
    @Query("SELECT * FROM item_table ORDER BY item_date DESC")
    fun allItems():List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Query("DELETE FROM item_table")
    fun deleteAll()
}