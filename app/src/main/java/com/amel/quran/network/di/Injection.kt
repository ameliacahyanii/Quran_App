package com.amel.quran.network.di

import android.content.Context
import com.amel.quran.local.CalendarPreferences
import com.amel.quran.local.LocationPreferences
import com.amel.quran.network.ApiConfig
import com.amel.quran.network.quran.QuranRepository
import com.amel.quran.network.RemoteDataSource
import com.amel.quran.network.adzan.AdzanRepository

object Injection {
    private val quranApiService = ApiConfig.getQuranService
    private val adzanApiService = ApiConfig.getAdzanTimeService
    private val remoteDataSource = RemoteDataSource(quranApiService, adzanApiService)
    fun provideQuranRepository(): QuranRepository {
        return QuranRepository(remoteDataSource)
    }

    fun provideAdzanRepository(context: Context): AdzanRepository {
        val locationPreferences = LocationPreferences(context)
        val calendarPrefrences = CalendarPreferences()
        return AdzanRepository(remoteDataSource, locationPreferences, calendarPrefrences)
    }
}