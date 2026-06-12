package com.work.todo.ui.calendar.task

import com.work.todo.ui.UiText

data class CalendarTaskItem(
    val id: Int,
    val date: UiText,
    val time: UiText,
    val title: String
)