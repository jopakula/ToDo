package com.work.todo.ui.calendar.task

import com.work.todo.ui.UiText

sealed class CalendarTasksState {
    object Loading : CalendarTasksState()
    object Empty : CalendarTasksState()
    data class Success(val tasks: List<CalendarTaskItem>) : CalendarTasksState()
    data class Error(val message: UiText) : CalendarTasksState()
}