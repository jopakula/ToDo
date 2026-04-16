package com.work.todo.ui.home.task

data class HomeTaskItem(
    val id: Int,
    val title: String,
    val time: String,
    var isDone: Boolean = false,
)