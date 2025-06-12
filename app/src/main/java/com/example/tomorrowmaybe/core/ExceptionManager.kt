package com.example.tomorrowmaybe.core

import kotlin.Exception

suspend fun <T> safeCall(block: suspend () -> T): ResultWrapper<T> {
    return try {
        val result = block()
        ResultWrapper.Success(result)
    } catch (e: Exception) {
        ResultWrapper.Error(mapToCustomException(e))
    }
}

fun mapToCustomException(e: Exception): Exception {
    return when (e) {

        is java.net.SocketException -> Exception("Tiempo de espera agotado")
        is java.net.UnknownHostException -> Exception("No hay conexion a internet")
        is com.google.firebase.FirebaseException -> Exception("Error de firebase: ${e.message}")
        else -> Exception("Error desconocido: ${e.message}")
    }
}