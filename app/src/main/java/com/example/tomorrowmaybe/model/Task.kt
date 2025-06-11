package com.example.tomorrowmaybe.model

data class Task(
    var id: String = "",
    var name: String = "",
    var title: String = "",
    var description: String = "",
    var dueDate: String= "",
    val isCompleted: Boolean = false
)