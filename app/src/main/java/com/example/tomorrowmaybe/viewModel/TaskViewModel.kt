package com.example.tomorrowmaybe.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomorrowmaybe.core.ResultWrapper
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.network.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _selectedTask = MutableLiveData<Task>()
    val selectedTask: LiveData<Task> get() = _selectedTask

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchTasks() {
        viewModelScope.launch {
            setLoading(true)
            when (val result = taskRepository.getAllTasks()) {
                is ResultWrapper.Success -> _tasks.value = result.data
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            setLoading(false)
        }
    }

    fun fetchTaskById(id: String) {
        viewModelScope.launch {
            setLoading(true)
            when (val result = taskRepository.getTask(id)) {
                is ResultWrapper.Success -> _selectedTask.value = result.data
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            setLoading(false)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            setLoading(true)
            when (val result = taskRepository.addTask(task)) {
                is ResultWrapper.Success -> fetchTasks()
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            setLoading(false)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            setLoading(true)
            when (val result = taskRepository.updateTask(task)) {
                is ResultWrapper.Success -> fetchTasks()
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            setLoading(false)
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            setLoading(true)
            when (val result = taskRepository.deleteTask(id)) {
                is ResultWrapper.Success -> fetchTasks()
                is ResultWrapper.Error -> _error.value = result.exception.message
            }
            setLoading(false)
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun setLoading(state: Boolean) {
        _loading.value = state
    }
}
