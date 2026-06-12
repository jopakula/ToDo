package com.work.todo.ui.mapper

import com.work.todo.domain.Task
import com.work.todo.ui.home.task.HomeTaskItem

object TaskMapper {
    fun mapToUi(entity: Task): HomeTaskItem {
        return HomeTaskItem(
            id = entity.id,
            title = entity.title,
            time = entity.time ?: "--:--",
            isDone = entity.isDone
        )
    }

    fun mapToUiList(entities: List<Task>): List<HomeTaskItem> {
        return entities.map { mapToUi(it) }
    }
}