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

class RegisterViewModel : ViewModel() {

    // Estados observables
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _validRegister = MutableLiveData<Boolean>()
    val validRegister: LiveData<Boolean> get() = _validRegister

    private val firebaseAuth = FirebaseAuth.getInstance()

    /**
     * Registra un nuevo usuario en Firebase
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     */
    fun requestSignUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _validRegister.value = false
            return
        }

        _loaderState.value = true // Mostrar loader

        viewModelScope.launch {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                // Pequeño delay para mejor UX
                delay(1000)

                result.user?.let {
                    Log.i("FirebaseAuth", "Usuario creado exitosamente")
                    _validRegister.value = true
                } ?: run {
                    Log.e("FirebaseAuth", "Error: Usuario nulo después de registro")
                    _validRegister.value = false
                }
            } catch (e: Exception) {
                Log.e("FirebaseAuth", "Error al registrar: ${e.message}")
                _validRegister.value = false
            } finally {
                _loaderState.value = false // Ocultar loader
            }
        }
    }
}