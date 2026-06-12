package com.work.todo.ui.mapper

import com.work.todo.R
import com.work.todo.data.utils.TaskDateTimeUtils
import com.work.todo.domain.Task
import com.work.todo.ui.UiText
import com.work.todo.ui.calendar.task.CalendarTaskItem

object CalendarMapper {
    fun mapToUi(entity: Task): CalendarTaskItem {
        val dateUi = if (entity.date != null) {
            UiText.DynamicString(TaskDateTimeUtils.formatDbDateToUi(entity.date))
        } else {
            UiText.ResourceString(R.string.task_date_empty)
        }

        val timeUi = if (entity.time != null) {
            UiText.DynamicString(entity.time)
        } else {
            UiText.ResourceString(R.string.task_time_empty)
        }

        return CalendarTaskItem(
            id = entity.id,
            date = dateUi,
            time = timeUi,
            title = entity.title
        )
    }

    fun mapToUiList(entities: List<Task>): List<CalendarTaskItem> {
        return entities.map { mapToUi(it) }
    }
}