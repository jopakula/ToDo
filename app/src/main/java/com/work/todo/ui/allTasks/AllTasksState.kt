package com.work.todo.ui.allTasks

sealed class AllTasksState {
    object Loading : AllTasksState()
    object Empty : AllTasksState()
    data class Success(
        val overdueTasks: List<AllTasksItem>,
        val regularTasks: List<AllTasksItem>
    ) : AllTasksState()

    data class Error(val message: String) : AllTasksState()
}