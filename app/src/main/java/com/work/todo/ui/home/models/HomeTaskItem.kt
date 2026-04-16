package com.work.todo.ui.home.models

data class HomeTaskItem(
    val id: Int,
    val title: String,
    val time: String,
    var isDone: Boolean = false,
)