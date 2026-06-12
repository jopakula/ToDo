package com.work.todo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val notes: String? = null,
    val category: TaskEntityCategory,
    val date: String? = null,
    val time: String? = null,
    val reminder: Boolean = false,
    val isDone: Boolean = false
)

enum class TaskEntityCategory {
    WORK,
    PERSONAL,
    SHOPPING,
    HEALTH,
}