package com.work.todo.ui.home.task

import com.work.todo.ui.UiText

data class HomeTaskItem(
    val id: Int,
    val title: String,
    val time: UiText,
    val isDone: Boolean = false,
)