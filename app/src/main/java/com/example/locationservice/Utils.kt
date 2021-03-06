package com.example.locationservice

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import java.util.*

@SuppressLint("SimpleDateFormat")
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

fun checkGpsStatus(context: Context) : Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        return true
    }
    return false
}
