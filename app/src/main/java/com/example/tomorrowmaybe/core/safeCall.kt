package com.example.tomorrowmaybe.core


import kotlin.Exception

suspend fun <T> safeCall(block: suspend () -> T): ResultWrapper<T> {
    return try {
        val result = block()
        ResultWrapper.Success(result)
    } catch (e: Exception) {
        ResultWrapper.Error((e))
    }
}