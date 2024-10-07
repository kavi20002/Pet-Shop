package com.example.petshop.taskModel

data class Task(
    var description: String,
    var time: String,
    var date: String,
    var isComplete: Boolean = false

)
