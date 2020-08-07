package com.example.locationservice

import android.content.Context
import android.icu.text.DisplayContext
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

private lateinit var instance: RoomSingleton
private lateinit var listItem: List<Item>
val adapter = ItemListAdapter()
private lateinit var layoutManager: LinearLayoutManager
private lateinit var recyclerView: RecyclerView

fun initData(context: Context, rv: RecyclerView) {
    layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.VERTICAL

    recyclerView = rv
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = adapter

    instance = RoomSingleton.getInstance(context)
}

fun coroutineGetData() {
    listItem = emptyList<Item>()
    listItem = instance.roomDAO().allItems()
    adapter.setItems(listItem)
}

fun getDateStr() : String {
    val sdf = SimpleDateFormat("dd/M/yyyy kk:mm:ss")
    return sdf.format(Date())
}

fun getAddress(context: Context, location: Location) : String {
    val geoCoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address> = geoCoder.getFromLocation(location.latitude.toDouble(), location.longitude.toDouble(), 1) as List<Address>

    val address = addresses.get(0).getAddressLine(0)

    return address.toString()
}

fun getCoordinates(location: Location, flag: Boolean) : String {
    if (flag) {
        return (location.latitude.toDouble().toString() + "°")
    } else {
        return (location.longitude.toDouble().toString() + "°")
    }
}