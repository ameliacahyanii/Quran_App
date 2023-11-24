package com.amel.quran.network

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Error(val errorMessage: String?) : NetworkResponse<Nothing>()
}
