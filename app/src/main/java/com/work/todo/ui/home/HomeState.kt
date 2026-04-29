package com.work.todo.ui.home

import com.work.todo.ui.home.task.HomeTaskItem

sealed class HomeState {
    object Empty : HomeState()
    object Loading : HomeState()
    data class Success(val tasks: List<HomeTaskItem>) : HomeState()
    data class Error(val message: String) : HomeState()
}