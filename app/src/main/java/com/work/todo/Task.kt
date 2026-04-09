package com.work.todo

import java.io.Serializable

data class Task(
    val title: String,
    val description: String
) : Serializable