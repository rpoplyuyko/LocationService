package com.example.locationservice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    val allWords: LiveData<List<Item>>
    var roomSingleton: RoomSingleton

    init {
        roomSingleton = RoomSingleton.getInstance(application)
        allWords = roomSingleton.roomDAO().allItems()
    }
}
