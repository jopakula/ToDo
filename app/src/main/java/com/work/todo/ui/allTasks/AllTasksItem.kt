package com.work.todo.ui.allTasks

data class AllTasksItem(
    val id: Int,
    val title: String,
    val dateTimeInfo: String,
    var isDone: Boolean = false,
    val isOverdue: Boolean = false
)