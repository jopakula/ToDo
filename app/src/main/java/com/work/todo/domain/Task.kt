package com.work.todo.domain

data class Task(
    val id: Int,
    val title: String,
    val notes: String?,
    val category: TaskCategory,
    val date: String?,
    val time: String?,
    val reminder: Boolean,
    val isDone: Boolean
)

enum class TaskCategory {
    WORK,
    PERSONAL,
    SHOPPING,
    HEALTH,
}