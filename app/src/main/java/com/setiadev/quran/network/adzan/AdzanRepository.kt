package com.setiadev.quran.network.adzan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.setiadev.quran.local.CalendarPreferences
import com.setiadev.quran.local.LocationPreferences
import com.setiadev.quran.network.NetworkBoundResource
import com.setiadev.quran.network.RemoteDataSource
import com.setiadev.quran.network.Resource
import com.setiadev.quran.network.NetworkResponse
import com.setiadev.quran.utils.DataMapper
import kotlinx.coroutines.flow.Flow

class AdzanRepository(
    private val remoteDataSource: RemoteDataSource,
    private val locationPreferences: LocationPreferences,
    private val calendarPreferences: CalendarPreferences
) : IAdzanRepository {
    override fun getLastKnownLocation(): LiveData<List<String>> =
        locationPreferences.getLastKnownLocation()

    override fun searchCity(city: String): Flow<Resource<List<City>>> {
        return object : NetworkBoundResource<List<City>, List<CityItem>>() {
            override fun fetchFromNetwork(data: List<CityItem>): Flow<List<City>> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<NetworkResponse<List<CityItem>>> {
                return remoteDataSource.searchCity(city)
            }
        }.asFlow()
    }

    override fun getDailyAdzanTime(
        id: String,
        year: String,
        month: String,
        date: String
    ): Flow<Resource<DailyAdzan>> {
        return object : NetworkBoundResource<DailyAdzan, JadwalItem>() {
            override fun fetchFromNetwork(data: JadwalItem): Flow<DailyAdzan> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<NetworkResponse<JadwalItem>> {
                return remoteDataSource.getDailyAdzanTime(id, year, month, date)
            }
        }.asFlow()
    }

    fun getResultDailyAdzanTime(): LiveData<Resource<AdzanDataResult>> {

        val liveDataLocation = getLastKnownLocation()
        val liveDataCityId = getLastKnownLocation().switchMap { listLocation ->
            searchCity(listLocation[0]).asLiveData()
        }
        val currentDate = calendarPreferences.getCurrentDate()
        val year = currentDate[0]
        val month = currentDate[1]
        val date = currentDate[2]
        val liveDataDailyAdzanTime = liveDataCityId.switchMap {
            val data = it.data?.get(0)?.id
            Log.i("Repository", "inside liveDataDailyAdzanTime: data id city is $data")
            if (data != null) getDailyAdzanTime(data, year, month, date).asLiveData()
            else getDailyAdzanTime("1301", year, month, date).asLiveData()
        }

        val result = MediatorLiveData<Resource<AdzanDataResult>>()

        result.addSource(liveDataLocation) {
            result.value = combineLatestData(liveDataLocation, liveDataDailyAdzanTime, currentDate)
        }
        result.addSource(liveDataCityId) {
            result.value = combineLatestData(liveDataLocation, liveDataDailyAdzanTime, currentDate)
        }
        result.addSource(liveDataDailyAdzanTime) {
            result.value = combineLatestData(liveDataLocation, liveDataDailyAdzanTime, currentDate)
        }
        return result
    }

    private fun combineLatestData(
        listLocationResult: LiveData<List<String>>,
        dailyAdzanTimeResult: LiveData<Resource<DailyAdzan>>,
        listCalendarResult: List<String>
    ): Resource<AdzanDataResult> {

        val listLocation = listLocationResult.value
        val dailyAdzanTime = dailyAdzanTimeResult.value

        // Don't send a success until we have both results
        return if (listLocation == null || dailyAdzanTime == null) {
            Resource.Loading()
        } else {
            try {
                Resource.Success(
                    AdzanDataResult(
                        listLocation = listLocation,
                        dailyAdzan = dailyAdzanTime,
                        listCalendar = listCalendarResult
                    )
                )
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage)
            }
        }
    }
}