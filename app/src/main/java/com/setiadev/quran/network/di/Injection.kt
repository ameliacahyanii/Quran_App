package com.setiadev.quran.network.di

import android.content.Context
import com.setiadev.quran.local.LocationPreferences
import com.setiadev.quran.network.ApiConfig
import com.setiadev.quran.network.quran.QuranRepository
import com.setiadev.quran.network.RemoteDataSource
import com.setiadev.quran.network.adzan.AdzanRepository

object Injection {
    fun provideQuranRepository(): QuranRepository {
        val quranApiService = ApiConfig.getQuranService
        val adzanApiService = ApiConfig.getAdzanTimeService
        val remoteDataSource = RemoteDataSource(quranApiService, adzanApiService)
        return QuranRepository(remoteDataSource)
    }

    fun provideAdzanRepository(context: Context): AdzanRepository {
        val quranApiService = ApiConfig.getQuranService
        val adzanApiService = ApiConfig.getAdzanTimeService
        val remoteDataSource = RemoteDataSource(quranApiService, adzanApiService)
        val locationPreferences = LocationPreferences(context)
        return AdzanRepository(remoteDataSource, locationPreferences)
    }
}