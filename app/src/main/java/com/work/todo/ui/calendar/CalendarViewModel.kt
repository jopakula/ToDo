package com.work.todo.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskDao
import com.work.todo.ui.mapper.CalendarMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val dbDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val _selectedDate = MutableStateFlow(dbDateFormatter.format(Date()))

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarState: StateFlow<CalendarState> = _selectedDate
        .flatMapLatest { date ->
            taskDao.getFlowTasksByDate(date).map { entities ->
                val uiTasks = CalendarMapper.mapToUiList(entities)

                if (uiTasks.isEmpty()) CalendarState.Empty else CalendarState.Success(uiTasks)
            }
        }
        .onStart {
            emit(CalendarState.Loading)
        }
        .catch { e ->
            emit(CalendarState.Error(e.message ?: "Unknown Error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CalendarState.Loading
        )

    fun selectDate(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        _selectedDate.value = dbDateFormatter.format(calendar.time)
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val task = taskDao.getTaskById(taskId)
                task?.let { taskDao.deleteTask(it) }
            } catch (e: Exception) {
            }
        }
    }
}