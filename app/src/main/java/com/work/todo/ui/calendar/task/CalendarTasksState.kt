package com.work.todo.ui.calendar.task

sealed class CalendarTasksState {
    object Loading : CalendarTasksState()
    object Empty : CalendarTasksState()
    data class Success(val tasks: List<CalendarTaskItem>) : CalendarTasksState()
    data class Error(val message: String) : CalendarTasksState()
}