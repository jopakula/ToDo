package com.work.todo.ui.mapper

import com.work.todo.R
import com.work.todo.domain.Task
import com.work.todo.ui.UiText
import com.work.todo.ui.home.task.HomeTaskItem

object TaskMapper {

    fun mapToUi(entity: Task): HomeTaskItem {
        val timeUi = if (entity.time != null) {
            UiText.DynamicString(entity.time)
        } else {
            UiText.ResourceString(R.string.task_time_empty)
        }

        return HomeTaskItem(
            id = entity.id,
            title = entity.title,
            time = timeUi,
            isDone = entity.isDone
        )
    }

    fun mapToUiList(entities: List<Task>): List<HomeTaskItem> {
        return entities.map { mapToUi(it) }
    }
}