package com.work.todo.ui.task.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskCategory
import com.work.todo.database.TaskDao
import com.work.todo.database.TaskEntity
import com.work.todo.ui.task.TaskState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val _state = MutableSharedFlow<TaskState>()
    val state: SharedFlow<TaskState> = _state

    private val _scheduleReminderEvent = MutableSharedFlow<Pair<Int, Long>>()
    val scheduleReminderEvent: SharedFlow<Pair<Int, Long>> = _scheduleReminderEvent

    fun saveTask(
        title: String,
        notes: String?,
        category: TaskCategory,
        date: String?,
        time: String?,
        reminder: Boolean
    ) {
        val task = TaskEntity(
            title = title,
            category = category,
            date = date,
            time = time,
            notes = notes,
            reminder = reminder
        )

        viewModelScope.launch {
            _state.emit(TaskState.Loading)
            try {
                val taskId = taskDao.insertTask(task).toInt()

                if (reminder && date != null && time != null) {
                    val timeInMillis = convertDateTimeToMillis(date, time)
                    if (timeInMillis != null) {
                        _scheduleReminderEvent.emit(Pair(taskId, timeInMillis))
                    }
                }

                _state.emit(TaskState.Success)
            } catch (e: Exception) {
                _state.emit(TaskState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    private fun convertDateTimeToMillis(dateStr: String, timeStr: String): Long? {
        return try {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

            val dateObj = dateFormatter.parse(dateStr)
            val timeObj = timeFormatter.parse(timeStr)

            if (dateObj != null && timeObj != null) {
                val dateCalendar = Calendar.getInstance().apply { time = dateObj }
                val timeCalendar = Calendar.getInstance().apply { time = timeObj }

                val finalCalendar = Calendar.getInstance()
                finalCalendar.set(
                    dateCalendar.get(Calendar.YEAR),
                    dateCalendar.get(Calendar.MONTH),
                    dateCalendar.get(Calendar.DAY_OF_MONTH),
                    timeCalendar.get(Calendar.HOUR_OF_DAY),
                    timeCalendar.get(Calendar.MINUTE),
                    0
                )
                finalCalendar.timeInMillis
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}