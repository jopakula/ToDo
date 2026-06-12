package com.work.todo.ui.allTasks.task

import com.work.todo.ui.UiText

data class AllTasksItem(
    val id: Int,
    val title: String,
    val dateTimeInfo: UiText,
    val isDone: Boolean = false,
    val isOverdue: Boolean = false
)