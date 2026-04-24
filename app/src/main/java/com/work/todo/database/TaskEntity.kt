package com.work.todo.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: TaskCategory,
    val date: String,
    val time: String,
    val reminder: Boolean = false,
    val notes: String,
    val isDone: Boolean = false
)

enum class TaskCategory {
    WORK,
    PERSONAL,
    SHOPPING,
    HEALTH,
}