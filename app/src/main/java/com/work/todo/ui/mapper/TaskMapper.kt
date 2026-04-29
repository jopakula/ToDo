package com.work.todo.ui.mapper

import com.work.todo.database.TaskEntity
import com.work.todo.ui.home.task.HomeTaskItem

object TaskMapper {
    fun mapToUi(entity: TaskEntity): HomeTaskItem {
        return HomeTaskItem(
            id = entity.id,
            title = entity.title,
            time = entity.time ?: "--:--",
            isDone = entity.isDone
        )
    }

    fun mapToUiList(entities: List<TaskEntity>): List<HomeTaskItem> {
        return entities.map { mapToUi(it) }
    }
}