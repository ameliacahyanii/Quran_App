package com.setiadev.quran.network.quran

import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {
    @GET("surah")
    suspend fun getListSurah(): SurahResponse

    @GET("surah/{number}/editions/quran-uthmani,ar.alafasy,id.indonesian")
    suspend fun getDetailSurahWithQuranEditions(
        @Path("number") numberSurah: Int
    ): AyahResponse
}