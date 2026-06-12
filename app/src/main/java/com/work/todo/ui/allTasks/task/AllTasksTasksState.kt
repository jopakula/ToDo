package com.work.todo.ui.allTasks.task

sealed class AllTasksTasksState {
    object Loading : AllTasksTasksState()
    object Empty : AllTasksTasksState()
    data class Success(
        val overdueTasks: List<AllTasksItem>,
        val regularTasks: List<AllTasksItem>
    ) : AllTasksTasksState()

    data class Error(val message: String) : AllTasksTasksState()
}