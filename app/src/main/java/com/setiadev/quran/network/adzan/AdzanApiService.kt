package com.setiadev.quran.network.adzan

import retrofit2.http.GET
import retrofit2.http.Path

interface AdzanApiService {
    @GET("sholat/kota/cari/{city}")
    suspend fun searchCity(
        @Path("city") city: String
    ): CityResponse

    @GET("sholat/jadwal/{id}/{year}/{month}/{date}")
    suspend fun getDailyAdzanTime(
        @Path("id") id: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("date") date: String
    ): DailyResponse
}