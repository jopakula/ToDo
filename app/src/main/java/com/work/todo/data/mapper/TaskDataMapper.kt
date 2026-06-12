package com.work.todo.data.mapper

import com.work.todo.data.database.TaskEntity
import com.work.todo.data.database.TaskEntityCategory
import com.work.todo.domain.Task
import com.work.todo.domain.TaskCategory

object TaskDataMapper {

    fun mapToDomain(entityTask: TaskEntity): Task = Task(
        id = entityTask.id,
        title = entityTask.title,
        notes = entityTask.notes,
        category = mapCategoryToDomain(entityCategory = entityTask.category),
        date = entityTask.date,
        time = entityTask.time,
        reminder = entityTask.reminder,
        isDone = entityTask.isDone
    )

    fun mapToEntity(domainTask: Task): TaskEntity = TaskEntity(
        id = domainTask.id,
        title = domainTask.title,
        notes = domainTask.notes,
        category = mapCategoryToEntity(domainCategory = domainTask.category),
        date = domainTask.date,
        time = domainTask.time,
        reminder = domainTask.reminder,
        isDone = domainTask.isDone
    )

    private fun mapCategoryToDomain(entityCategory: TaskEntityCategory): TaskCategory {
        return when (entityCategory) {
            TaskEntityCategory.WORK -> TaskCategory.WORK
            TaskEntityCategory.PERSONAL -> TaskCategory.PERSONAL
            TaskEntityCategory.SHOPPING -> TaskCategory.SHOPPING
            TaskEntityCategory.HEALTH -> TaskCategory.HEALTH
        }
    }

    fun mapCategoryToEntity(domainCategory: TaskCategory): TaskEntityCategory {
        return when (domainCategory) {
            TaskCategory.WORK -> TaskEntityCategory.WORK
            TaskCategory.PERSONAL -> TaskEntityCategory.PERSONAL
            TaskCategory.SHOPPING -> TaskEntityCategory.SHOPPING
            TaskCategory.HEALTH -> TaskEntityCategory.HEALTH
        }
    }
}