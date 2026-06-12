package com.work.todo.ui.allTasks

import com.work.todo.ui.allTasks.task.AllTasksItem

sealed class AllTasksState {
    object Loading : AllTasksState()
    object Empty : AllTasksState()
    data class Success(
        val overdueTasks: List<AllTasksItem>,
        val regularTasks: List<AllTasksItem>
    ) : AllTasksState()

    data class Error(val message: String) : AllTasksState()
}