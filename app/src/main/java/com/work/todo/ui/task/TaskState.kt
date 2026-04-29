package com.work.todo.ui.task

sealed class TaskState {
    object Idle : TaskState()
    object Loading : TaskState()
    object Success : TaskState()
    data class Error(val message: String) : TaskState()
}