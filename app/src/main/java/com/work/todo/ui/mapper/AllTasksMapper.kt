package com.work.todo.ui.mapper

import com.work.todo.database.TaskEntity
import com.work.todo.ui.allTasks.AllTasksItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AllTasksMapper {
    fun mapToUi(entity: TaskEntity): AllTasksItem {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val isOverdue = entity.date != null && entity.date < today && !entity.isDone

        return AllTasksItem(
            id = entity.id,
            title = entity.title,
            dateTimeInfo = "${entity.date ?: ""} ${entity.time ?: ""}".trim(),
            isDone = entity.isDone,
            isOverdue = isOverdue
        )
    }

    fun mapToUiList(entities: List<TaskEntity>): List<AllTasksItem> {
        return entities.map { mapToUi(it) }
    }
}