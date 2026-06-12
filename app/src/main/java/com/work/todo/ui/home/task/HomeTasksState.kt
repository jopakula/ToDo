package com.work.todo.ui.home.task

import com.work.todo.ui.UiText

sealed class HomeTasksState {
    object Loading : HomeTasksState()
    object Empty : HomeTasksState()
    data class Success(val tasks: List<HomeTaskItem>) : HomeTasksState()
    data class Error(val message: UiText) : HomeTasksState()
}