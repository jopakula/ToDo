package com.work.todo.ui.mapper

import com.work.todo.R
import com.work.todo.data.utils.TaskDateTimeUtils
import com.work.todo.domain.Task
import com.work.todo.ui.UiText
import com.work.todo.ui.allTasks.task.AllTasksItem

object AllTasksMapper {

    fun mapToUi(task: Task, todayDbDate: String): AllTasksItem {
        val isOverdue = task.date != null && task.date < todayDbDate && !task.isDone

        val dateTimeUi = when {
            task.date != null && task.time != null -> {
                val formattedDate = TaskDateTimeUtils.formatDbDateToUi(task.date)
                UiText.DynamicString("$formattedDate, ${task.time}")
            }

            task.date != null -> {
                UiText.DynamicString(TaskDateTimeUtils.formatDbDateToUi(task.date))
            }

            task.time != null -> {
                UiText.DynamicString(task.time)
            }

            else -> {
                UiText.ResourceString(R.string.task_no_datetime)
            }
        }

        return AllTasksItem(
            id = task.id,
            title = task.title,
            dateTimeInfo = dateTimeUi,
            isDone = task.isDone,
            isOverdue = isOverdue
        )
    }

    fun mapToUiList(tasks: List<Task>, todayDbDate: String): List<AllTasksItem> {
        return tasks.map { mapToUi(it, todayDbDate) }
    }
}