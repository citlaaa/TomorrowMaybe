package com.example.tomorrowmaybe.model

data class Task(
    val id: String = "",
    val title: String,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean = false
)