package com.setiadev.quran.network.adzan

import androidx.lifecycle.LiveData
import com.setiadev.quran.network.Resource
import kotlinx.coroutines.flow.Flow

interface IAdzanRepository {
    fun getLastKnownLocation(): LiveData<List<String>>
    fun searchCity(city: String): Flow<Resource<List<City>>>

    fun getDailyAdzanTime(
        id: String,
        year: String,
        month: String,
        date: String
    ): Flow<Resource<DailyAdzan>>
}