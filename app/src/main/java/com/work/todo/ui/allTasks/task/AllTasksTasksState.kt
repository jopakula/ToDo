package com.work.todo.ui.allTasks.task

import com.work.todo.ui.UiText

sealed class AllTasksTasksState {
    object Loading : AllTasksTasksState()
    object Empty : AllTasksTasksState()
    data class Success(
        val overdueTasks: List<AllTasksItem>,
        val regularTasks: List<AllTasksItem>
    ) : AllTasksTasksState()

    data class Error(val message: UiText) : AllTasksTasksState()
}