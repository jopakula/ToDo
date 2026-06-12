package com.work.todo.ui.allTasks.task

data class AllTasksUiState(
    val tasksState: AllTasksTasksState = AllTasksTasksState.Loading,
    val searchQuery: String = ""
)
