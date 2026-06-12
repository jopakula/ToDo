package com.work.todo.data

import com.work.todo.data.database.TaskDao
import com.work.todo.data.mapper.TaskDataMapper
import com.work.todo.domain.Task
import com.work.todo.domain.TaskCategory
import com.work.todo.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getFlowAllTasks(): Flow<List<Task>> =
        taskDao.getFlowAllTasks()
            .map { list -> list.map { TaskDataMapper.mapToDomain(entityTask = it) } }

    override fun getFlowTasksByDate(date: String): Flow<List<Task>> =
        taskDao.getFlowTasksByDate(date)
            .map { list -> list.map { TaskDataMapper.mapToDomain(entityTask = it) } }

    override fun getFlowTasksByCategory(category: TaskCategory): Flow<List<Task>> {
        val entityCategory = TaskDataMapper.mapCategoryToEntity(domainCategory = category)
        return taskDao.getFlowTasksByCategory(entityCategory).map { list ->
            list.map { TaskDataMapper.mapToDomain(entityTask = it) }
        }
    }

    override suspend fun getTaskById(taskId: Int): Task? =
        taskDao.getTaskById(taskId)?.let { TaskDataMapper.mapToDomain(entityTask = it) }

    override suspend fun insertTask(task: Task): Long =
        taskDao.insertTask(TaskDataMapper.mapToEntity(domainTask = task))

    override suspend fun updateTask(task: Task) =
        taskDao.updateTask(TaskDataMapper.mapToEntity(domainTask = task))

    override suspend fun updateTaskStatus(taskId: Int, isDone: Boolean) =
        taskDao.updateTaskStatus(taskId, isDone)

    override suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(TaskDataMapper.mapToEntity(domainTask = task))
}