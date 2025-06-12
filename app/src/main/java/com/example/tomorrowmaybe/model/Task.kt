package com.example.tomorrowmaybe.model

import androidx.annotation.Keep
import java.util.Date

@Keep
data class Task(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val date: Date = Date()

)