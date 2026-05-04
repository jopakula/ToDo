package com.work.todo.ui.task.editTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskCategory
import com.work.todo.database.TaskDao
import com.work.todo.database.TaskEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val _taskState = MutableStateFlow<TaskEntity?>(null)
    val taskState: StateFlow<TaskEntity?> = _taskState

    private val _scheduleReminderEvent = MutableSharedFlow<Pair<Int, Long>>()
    val scheduleReminderEvent: SharedFlow<Pair<Int, Long>> = _scheduleReminderEvent

    private val _cancelReminderEvent = MutableSharedFlow<Int>()
    val cancelReminderEvent: SharedFlow<Int> = _cancelReminderEvent

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            val task = taskDao.getTaskById(taskId)
            _taskState.value = task
        }
    }

    fun updateTask(
        title: String,
        notes: String?,
        category: TaskCategory,
        date: String?,
        time: String?,
        reminder: Boolean
    ) {
        val currentTask = _taskState.value ?: return
        viewModelScope.launch {
            val updatedTask = currentTask.copy(
                title = title,
                notes = notes,
                category = category,
                date = date,
                time = time,
                reminder = reminder
            )
            taskDao.updateTask(updatedTask)

            if (reminder && date != null && time != null) {
                val timeInMillis = convertDateTimeToMillis(date, time)
                if (timeInMillis != null) {
                    _scheduleReminderEvent.emit(Pair(currentTask.id, timeInMillis))
                }
            } else {
                _cancelReminderEvent.emit(currentTask.id)
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
                val calendar = Calendar.getInstance()
                val dateCal = Calendar.getInstance().apply { time = dateObj }
                val timeCal = Calendar.getInstance().apply { time = timeObj }
                calendar.set(
                    dateCal.get(Calendar.YEAR),
                    dateCal.get(Calendar.MONTH),
                    dateCal.get(Calendar.DAY_OF_MONTH),
                    timeCal.get(Calendar.HOUR_OF_DAY),
                    timeCal.get(Calendar.MINUTE),
                    0
                )
                calendar.timeInMillis
            } else null
        } catch (e: Exception) {
            null
        }
    }
}