package com.amel.quran.local

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
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
            if (Build.VERSION.SDK_INT >= 33) {
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                ) { listAddress ->
                    // Provide data for search city
                    val city = listAddress[0].subAdminArea
                    val cityListName = city.split(" ")

                    val currentLanguage = Locale.getDefault().language
                    Log.i("LocPref",
                         "currentLanguage: $currentLanguage"
                    )

                    val resultOfCity = when (currentLanguage) {
                        "in" -> getNameOfCity(cityListName, false)
                        "en" -> getNameOfCity(cityListName, true)
                        else -> "Jakarta"
                    }
                    Log.i("LocPref", "City Name: $resultOfCity")

                    val subLocality = listAddress[0].subLocality
                    val locality = listAddress[0].locality
                    val address = "$subLocality, $locality"
                    Log.i("LocPref", "Address: $address")

                    val listCity = listOf(resultOfCity, address)
                    Log.i("LocPref", "getLastKnownLocation: $listCity")
                    lastKnownLocation.postValue(listCity)
                }
            } else {
                @Suppress("DEPRECATION") val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val city = listAddress?.get(0)?.subAdminArea
                val cityLastName = city?.split(" ")

                val currentLanguage = Locale.getDefault().language
                Log.i("LocPref", "currentLanguage: $currentLanguage")

                val resultOfCity = if (cityLastName != null) {
                    when (currentLanguage) {
                        "in" -> getNameOfCity(cityLastName, false)
                        "en" -> getNameOfCity(cityLastName, true)
                        else -> "Jakarta"
                    }
                } else {
                    "Jakarta"
                }
                Log.i("LocPref", "City Name: $resultOfCity")

                val subLocality = listAddress?.get(0)?.subLocality
                val locality = listAddress?.get(0)?.locality
                val  address = "$subLocality, $locality"
                Log.i("LocPref", "Address: $address")

                val listCity = listOf(resultOfCity, address)
                Log.i("LocPref", "getLastKnownLocation: $listCity")
                lastKnownLocation.postValue(listCity)
            }

//            fusedLocationClient.lastLocation.addOnFailureListener { exception ->
//                Log.e("LocPref", "requestLocationUpdates: " + exception.localizedMessage)
//            }
        }
        return lastKnownLocation
    }

    private fun getNameOfCity(phrase: List<String>, isEnglish: Boolean): String {
        var result = ""
        if (isEnglish) {
            for (i in 0 until phrase.size - 1) {
                result += phrase[i]
            }
        } else {
            for (i in 1 until phrase.size) {
                result += phrase[i]
            }
        }
        return result
    }
}