package com.example.tomorrowmaybe.network

import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import com.example.tomorrowmaybe.core.ResultWrapper
import com.example.tomorrowmaybe.model.Task
import com.example.tomorrowmaybe.core.safeCall
import kotlinx.coroutines.tasks.await


class TaskRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val taskCollection = firestore.collection("Tasks")

    suspend fun addTask(task: Task): ResultWrapper<Void> = safeCall {
        val id = taskCollection.document().id
        task.id = id
        taskCollection.document(id).set(task).await()
    }

    suspend fun getTask(id: String): ResultWrapper<Task> = safeCall {
        val document = taskCollection.document(id).get().await()
        document.toObject(Task::class.java) ?: throw Exception("Tarea no encontrada")
    }

    suspend fun getAllTasks(): ResultWrapper<List<Task>> = safeCall {
        val snapshot = taskCollection.get().await()
        snapshot.toObjects(Task::class.java)
    }

    suspend fun updateTask(task: Task): ResultWrapper<Void> = safeCall {
        taskCollection.document(task.id).set(task).await()
    }

    suspend fun deleteTask(id: String): ResultWrapper<Void> = safeCall {
        taskCollection.document(id).delete().await()
    }
}