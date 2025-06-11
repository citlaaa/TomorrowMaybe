package com.example.tomorrowmaybe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    // Estados observables
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean> get() = _sessionValid

    private val firebaseAuth = FirebaseAuth.getInstance()

    /**
     * Intenta autenticar al usuario con Firebase
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     */
    fun requestSingnIn(email: String, password: String) {
        _loaderState.value = true // Mostrar loader

        viewModelScope.launch {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

                // Pequeño delay para mejor UX
                delay(1000)

                result.user?.let {
                    _sessionValid.value = true // Autenticación exitosa
                } ?: run {
                    Log.e("FirebaseAuth", "Error: Usuario nulo")
                    _sessionValid.value = false
                }
            } catch (e: Exception) {
                Log.e("FirebaseAuth", "Error al iniciar sesión: ${e.message}")
                _sessionValid.value = false
            } finally {
                _loaderState.value = false // Ocultar loader
            }
        }
    }
}