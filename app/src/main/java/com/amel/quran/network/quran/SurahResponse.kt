package com.amel.quran.network.quran

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurahResponse(
    @Json(name="code")
    val code: Int? = null,

    @Json(name="data")
    val listSurah: List<SurahItem>,

    @Json(name="status")
    val status: String? = null
)

@JsonClass(generateAdapter = true)
data class SurahItem(

    @Json(name="number")
    val number: Int? = null,

    @Json(name="englishName")
    val englishName: String? = null,

    @Json(name="numberOfAyahs")
    val numberOfAyahs: Int? = null,

    @Json(name="revelationType")
    val revelationType: String? = null,

    @Json(name="name")
    val name: String? = null,

    @Json(name="englishNameTranslation")
    val englishNameTranslation: String? = null
)
