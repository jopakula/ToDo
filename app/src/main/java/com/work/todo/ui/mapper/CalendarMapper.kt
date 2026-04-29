package com.work.todo.ui.mapper

import com.work.todo.database.TaskEntity
import com.work.todo.ui.calendar.CalendarTaskItem

object CalendarMapper {
    fun mapToUi(entity: TaskEntity): CalendarTaskItem {
        return CalendarTaskItem(
            id = entity.id,
            date = entity.date ?: "",
            time = entity.time ?: "--:--",
            title = entity.title
        )
    }

    fun mapToUiList(entities: List<TaskEntity>): List<CalendarTaskItem> {
        return entities.map { mapToUi(it) }
    }
}