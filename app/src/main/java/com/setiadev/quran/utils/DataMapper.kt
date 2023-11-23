package com.setiadev.quran.utils

import com.setiadev.quran.network.adzan.City
import com.setiadev.quran.network.adzan.CityItem
import com.setiadev.quran.network.adzan.DailyAdzan
import com.setiadev.quran.network.adzan.JadwalItem
import com.setiadev.quran.network.quran.Ayah
import com.setiadev.quran.network.quran.AyahsItem
import com.setiadev.quran.network.quran.QuranEdition
import com.setiadev.quran.network.quran.QuranEditionItem
import com.setiadev.quran.network.quran.Surah
import com.setiadev.quran.network.quran.SurahItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {

    @JvmName("mapSurahResponseToDomain")
    fun mapResponseToDomain(input: List<SurahItem>): Flow<List<Surah>> {
        val listSurah = ArrayList<Surah>()
        input.map {
            val surah = Surah(
                number = it.number,
                name = it.name,
                englishName = it.englishName,
                englishNameTranslation = it.englishNameTranslation,
                numberOfAyahs = it.numberOfAyahs,
                revelationType = it.revelationType
            )
            listSurah.add(surah)
        }
        return flowOf(listSurah)
    }

    @JvmName("mapQuranEditionResponseToDomain")
    fun mapResponseToDomain(input: List<QuranEditionItem>): Flow<List<QuranEdition>> {
        val listQuranEdition = ArrayList<QuranEdition>()
        input.map {
            val quranEdition = QuranEdition(
                number = it.number,
                name = it.name,
                englishName = it.englishName,
                englishNameTranslation = it.englishNameTranslation,
                revelationType = it.revelationType,
                numberOfAyahs = it.numberOfAyahs,
                listAyahs = mapListAyahResponseToDomain(it.listAyahs)
            )
            listQuranEdition.add(quranEdition)
        }
        return flowOf(listQuranEdition)
    }

    private fun mapListAyahResponseToDomain(input: List<AyahsItem>): List<Ayah> {
        val listAyah = ArrayList<Ayah>()
        input.map {
            val ayah = Ayah(
                number = it.number,
                text = it.text,
                numberInSurah = it.numberInSurah,
                audio = it.audio
            )
            listAyah.add(ayah)
        }
        return listAyah
    }

    @JvmName("mapCityResponseToDomain")
    fun mapResponseToDomain(input: List<CityItem>): Flow<List<City>> {
        val listCity = ArrayList<City>()
        input.map {
            val city = City(
                lokasi = it.lokasi,
                id = it.id
            )
            listCity.add(city)
        }
        return flowOf(listCity)
    }

    @JvmName("mapDailyResponseToDomain")
    fun mapResponseToDomain(input: JadwalItem): Flow<DailyAdzan> {
        val dailyAdzan = DailyAdzan(
            date = input.date,
            imsak = input.imsak,
            isya = input.isya,
            dzuhur = input.dzuhur,
            subuh = input.subuh,
            dhuha = input.dhuha,
            terbit = input.terbit,
            tanggal = input.tanggal,
            ashar = input.ashar,
            maghrib = input.maghrib
        )
        return flowOf(dailyAdzan)
    }
}