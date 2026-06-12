package com.work.todo.ui.mapper

import com.work.todo.domain.Task
import com.work.todo.ui.allTasks.task.AllTasksItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AllTasksMapper {
    fun mapToUi(task: Task): AllTasksItem {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val isOverdue = task.date != null && task.date < today && !task.isDone

        return AllTasksItem(
            id = task.id,
            title = task.title,
            dateTimeInfo = "${task.date ?: ""} ${task.time ?: ""}".trim(),
            isDone = task.isDone,
            isOverdue = isOverdue
        )
    }

    fun mapToUiList(tasks: List<Task>): List<AllTasksItem> {
        return tasks.map { mapToUi(it) }
    }
}