package com.work.todo.ui.mapper

import com.work.todo.domain.Task
import com.work.todo.ui.calendar.task.CalendarTaskItem

object CalendarMapper {
    fun mapToUi(entity: Task): CalendarTaskItem {
        return CalendarTaskItem(
            id = entity.id,
            date = entity.date ?: "",
            time = entity.time ?: "--:--",
            title = entity.title
        )
    }

    fun mapToUiList(entities: List<Task>): List<CalendarTaskItem> {
        return entities.map { mapToUi(it) }
    }
}