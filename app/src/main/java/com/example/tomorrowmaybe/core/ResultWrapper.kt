package com.example.tomorrowmaybe.core

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T): ResultWrapper<T>()
    data class Error(val exception: Throwable): ResultWrapper<Nothing>()
}