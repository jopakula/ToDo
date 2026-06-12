package com.work.todo.ui.home.task

data class HomeTaskItem(
    val id: Int,
    val title: String,
    val time: String,
    val isDone: Boolean = false,
)