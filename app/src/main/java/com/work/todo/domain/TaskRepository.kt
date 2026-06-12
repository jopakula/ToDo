package com.work.todo.domain

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getFlowAllTasks(): Flow<List<Task>>
    fun getFlowTasksByDate(date: String): Flow<List<Task>>
    fun getFlowTasksByCategory(category: TaskCategory): Flow<List<Task>>
    suspend fun getTaskById(taskId: Int): Task?
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun updateTaskStatus(taskId: Int, isDone: Boolean)
    suspend fun deleteTask(task: Task)
}