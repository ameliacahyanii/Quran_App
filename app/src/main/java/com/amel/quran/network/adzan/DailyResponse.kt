package com.amel.quran.network.adzan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyResponse(

    @Json(name = "data")
    val dailyData: DailyData,

    @Json(name = "status")
    val status: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class DailyData(

    @Json(name = "jadwal")
    val jadwalItem: JadwalItem,

    @Json(name = "lokasi")
    val lokasi: String? = null,

    @Json(name = "daerah")
    val daerah: String? = null,

    @Json(name = "id")
    val id: String? = null,
)

@JsonClass(generateAdapter = true)
data class JadwalItem(

    @Json(name = "date")
    val date: String? = null,

    @Json(name = "imsak")
    val imsak: String? = null,

    @Json(name = "isya")
    val isya: String? = null,

    @Json(name = "dzuhur")
    val dzuhur: String? = null,

    @Json(name = "subuh")
    val subuh: String? = null,

    @Json(name = "dhuha")
    val dhuha: String? = null,

    @Json(name = "terbit")
    val terbit: String? = null,

    @Json(name = "tanggal")
    val tanggal: String? = null,

    @Json(name = "ashar")
    val ashar: String? = null,

    @Json(name = "maghrib")
    val maghrib: String? = null
)