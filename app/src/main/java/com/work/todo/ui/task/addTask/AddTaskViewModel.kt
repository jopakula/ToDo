package com.work.todo.ui.task.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.data.utils.TaskDateTimeUtils
import com.work.todo.domain.Task
import com.work.todo.domain.TaskCategory
import com.work.todo.domain.TaskRepository
import com.work.todo.ui.home.category.CategoryItem
import com.work.todo.ui.mapper.CategoryMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTaskUiState())
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()

    private val _scheduleReminderEvent = MutableSharedFlow<Pair<Int, Long>>()
    val scheduleReminderEvent: SharedFlow<Pair<Int, Long>> = _scheduleReminderEvent.asSharedFlow()

    init {
        val defaultCategory =
            CategoryMapper.getUiCategories().firstOrNull { it.categoryType == TaskCategory.WORK }
        _uiState.update { it.copy(selectedCategory = defaultCategory) }
    }

    fun onTitleChanged(title: String) {
        _uiState.update { it.copy(title = title, error = null) }
    }

    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun toggleCategoryMenu() {
        _uiState.update { it.copy(isCategoryMenuExpanded = !it.isCategoryMenuExpanded) }
    }

    fun selectCategory(categoryItem: CategoryItem) {
        _uiState.update { it.copy(selectedCategory = categoryItem, isCategoryMenuExpanded = false) }
    }

    fun toggleReminder() {
        _uiState.update { it.copy(isReminderEnabled = !it.isReminderEnabled) }
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        val (uiDate, dbDate) = TaskDateTimeUtils.getFormattedDatePair(year, month, day)
        _uiState.update {
            it.copy(
                formattedDate = uiDate,
                rawDate = dbDate
            )
        }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        val formattedTime = TaskDateTimeUtils.getFormattedTime(hour, minute)
        _uiState.update {
            it.copy(
                formattedTime = formattedTime,
                rawTime = formattedTime
            )
        }
    }

    fun saveTask() {
        val currentState = _uiState.value
        if (currentState.title.isBlank()) {
            _uiState.update { it.copy(error = "Введите название задачи") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = withContext(Dispatchers.IO) {
                    val task = Task(
                        id = 0,
                        title = currentState.title,
                        category = currentState.selectedCategory?.categoryType ?: TaskCategory.WORK,
                        date = currentState.rawDate,
                        time = currentState.rawTime,
                        notes = currentState.notes.ifEmpty { null },
                        reminder = currentState.isReminderEnabled,
                        isDone = false
                    )
                    val taskId = taskRepository.insertTask(task).toInt()

                    var timeInMillis: Long? = null
                    if (currentState.isReminderEnabled && currentState.rawDate != null && currentState.rawTime != null) {
                        timeInMillis = TaskDateTimeUtils.convertDateTimeToMillis(
                            currentState.rawDate,
                            currentState.rawTime
                        )
                    }
                    Pair(taskId, timeInMillis)
                }

                val (taskId, timeInMillis) = result
                if (timeInMillis != null) {
                    _scheduleReminderEvent.emit(Pair(taskId, timeInMillis))
                }
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown Error") }
            }
        }
    }
}