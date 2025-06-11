package com.example.tomorrowmaybe.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomorrowmaybe.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    // Estados para la UI
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _selectedTask = MutableLiveData<Task?>()
    val selectedTask: LiveData<Task?> get() = _selectedTask

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Datos de ejemplo (reemplazar con tu fuente real de datos)
    private val sampleTasks = listOf(
        Task("1", "Comprar víveres", "Leche, huevos y pan", "2023-06-15"),
        Task("2", "Reunión con equipo", "Preparar presentación", "2023-06-16"),
        Task("3", "Hacer ejercicio", "30 minutos de cardio", "2023-06-14")
    )

    init {
        loadTasks()
    }

    fun loadTasks() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Simular carga de red/base de datos
                delay(1000)
                _tasks.value = sampleTasks
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar tareas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectTask(task: Task) {
        _selectedTask.value = task
    }

    fun saveTask(task: Task) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Simular operación de guardado
                delay(500)
                // Aquí iría tu lógica real de guardado
                if (task.id.isEmpty()) {
                    // Nueva tarea
                    val newTask = task.copy(id = (sampleTasks.size + 1).toString())
                    _tasks.value = _tasks.value?.plus(newTask) ?: listOf(newTask)
                } else {
                    // Tarea existente
                    _tasks.value = _tasks.value?.map { if (it.id == task.id) task else it }
                }
                _selectedTask.value = task
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(task: Task) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Simular operación de eliminación
                delay(500)
                _tasks.value = _tasks.value?.filter { it.id != task.id }
                if (_selectedTask.value?.id == task.id) {
                    _selectedTask.value = null
                }
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}