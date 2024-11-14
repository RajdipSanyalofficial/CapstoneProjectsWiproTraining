package com.example.taskmanagerappdemo

data class Task(
    val id: Long = 0,
    var title: String,
    var description: String,
    var completed: Boolean = false,
    var hour: Int = 0,
    var minute: Int = 0 // Add minute
)