package com.setiadev.quran.local

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationPreferences(val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(): LiveData<List<String>> {
        val lastKnownLocation = MutableLiveData<List<String>>()
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val geocoder = Geocoder(context, Locale.getDefault())
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            ) { listAddress ->
                // Provide data for search city
                val city = listAddress[0].subAdminArea
                val cityListName = city.split(" ")

                val resultOfCity = if (cityListName.size > 1) cityListName[1]
                else cityListName[0]
                Log.i("LocationPreferences", "City Name: $resultOfCity")

                val subLocality = listAddress[0].subLocality
                val locality = listAddress[0].locality
                val address = "$subLocality, $locality"
                Log.i("LocationPreferences", "Address: $address")

                lastKnownLocation.postValue(listOf(resultOfCity, address))
            }

            fusedLocationClient.lastLocation.addOnFailureListener { exception ->
                Log.e("SharedViewModel", "requestLocationUpdates: " + exception.localizedMessage)
            }
        }
        return lastKnownLocation
    }
}