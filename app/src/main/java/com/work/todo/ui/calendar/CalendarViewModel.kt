package com.work.todo.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.data.utils.TaskDateTimeUtils
import com.work.todo.domain.TaskRepository
import com.work.todo.ui.calendar.task.CalendarTasksState
import com.work.todo.ui.calendar.task.CalendarUiState
import com.work.todo.ui.mapper.CalendarMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(TaskDateTimeUtils.getCurrentDbDate())

    val uiState: StateFlow<CalendarUiState> = _selectedDate
        .flatMapLatest { date ->
            taskRepository.getFlowTasksByDate(date).map { tasks ->
                val uiTasks = CalendarMapper.mapToUiList(tasks)
                val tasksState =
                    if (uiTasks.isEmpty()) CalendarTasksState.Empty else CalendarTasksState.Success(
                        uiTasks
                    )
                CalendarUiState(tasksState = tasksState, selectedDateRaw = date)
            }
        }
        .onStart {
            emit(CalendarUiState(tasksState = CalendarTasksState.Loading))
        }
        .catch { e ->
            emit(
                CalendarUiState(
                    tasksState = CalendarTasksState.Error(
                        e.message ?: "Unknown Error"
                    )
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CalendarUiState()
        )

    fun selectDate(year: Int, month: Int, day: Int) {
        _selectedDate.value = TaskDateTimeUtils.formatComponentsToDbDate(year, month, day)
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val task = taskRepository.getTaskById(taskId)
                    task?.let { taskRepository.deleteTask(it) }
                }
            } catch (e: Exception) {
                _selectedDate.update { currentDate ->
                    _selectedDate.value = currentDate
                    currentDate
                }
            }
        }
    }
}