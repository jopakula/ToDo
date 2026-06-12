package com.work.todo.ui.allTasks.task

data class AllTasksItem(
    val id: Int,
    val title: String,
    val dateTimeInfo: String,
    val isDone: Boolean = false,
    val isOverdue: Boolean = false
)