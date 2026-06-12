package com.work.todo.ui.calendar.task

import com.work.todo.data.utils.TaskDateTimeUtils

data class CalendarUiState(
    val tasksState: CalendarTasksState = CalendarTasksState.Loading,
    val selectedDateRaw: String = TaskDateTimeUtils.getCurrentDbDate()
)