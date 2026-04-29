package com.work.todo.ui.calendar

sealed class CalendarState {
    object Loading : CalendarState()
    object Empty : CalendarState()
    data class Success(val tasks: List<CalendarTaskItem>) : CalendarState()
    data class Error(val message: String) : CalendarState()
}