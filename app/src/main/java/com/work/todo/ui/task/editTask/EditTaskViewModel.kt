package com.work.todo.ui.task.editTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.R
import com.work.todo.data.utils.TaskDateTimeUtils
import com.work.todo.domain.Task
import com.work.todo.domain.TaskCategory
import com.work.todo.domain.TaskRepository
import com.work.todo.ui.UiText
import com.work.todo.ui.home.category.CategoryItem
import com.work.todo.ui.mapper.CategoryMapper
import com.work.todo.ui.task.TaskUiState
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

class EditTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private val _scheduleReminderEvent = MutableSharedFlow<Pair<Int, Long>>()
    val scheduleReminderEvent: SharedFlow<Pair<Int, Long>> = _scheduleReminderEvent.asSharedFlow()

    private val _cancelReminderEvent = MutableSharedFlow<Int>()
    val cancelReminderEvent: SharedFlow<Int> = _cancelReminderEvent.asSharedFlow()

    private var cachedTask: Task? = null

    fun loadTask(taskId: Int) {
        if (cachedTask != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val task = withContext(Dispatchers.IO) {
                taskRepository.getTaskById(taskId)
            }

            if (task != null) {
                cachedTask = task
                val categories = CategoryMapper.getUiCategories()
                val uiCategory = categories.firstOrNull { it.categoryType == task.category }

                _uiState.update {
                    it.copy(
                        title = task.title,
                        notes = task.notes.orEmpty(),
                        selectedCategory = uiCategory,
                        formattedDate = TaskDateTimeUtils.formatDbDateToUi(task.date),
                        formattedTime = task.time,
                        isReminderEnabled = task.reminder,
                        rawDate = task.date,
                        rawTime = task.time,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = UiText.ResourceString(R.string.error_task_not_found)
                    )
                }
            }
        }
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
        _uiState.update { it.copy(formattedDate = uiDate, rawDate = dbDate) }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        val formattedTime = TaskDateTimeUtils.getFormattedTime(hour, minute)
        _uiState.update { it.copy(formattedTime = formattedTime, rawTime = formattedTime) }
    }

    fun updateTask() {
        val currentTask = cachedTask ?: return
        val currentState = _uiState.value

        if (currentState.title.isBlank()) {
            _uiState.update { it.copy(error = UiText.ResourceString(R.string.error_empty_title)) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val timeInMillis = withContext(Dispatchers.IO) {
                    val updatedTask = currentTask.copy(
                        title = currentState.title,
                        notes = currentState.notes.ifEmpty { null },
                        category = currentState.selectedCategory?.categoryType ?: TaskCategory.WORK,
                        date = currentState.rawDate,
                        time = currentState.rawTime,
                        reminder = currentState.isReminderEnabled
                    )

                    taskRepository.updateTask(updatedTask)

                    if (currentState.isReminderEnabled && currentState.rawDate != null && currentState.rawTime != null) {
                        TaskDateTimeUtils.convertDateTimeToMillis(
                            currentState.rawDate,
                            currentState.rawTime
                        )
                    } else null
                }

                if (timeInMillis != null) {
                    _scheduleReminderEvent.emit(Pair(currentTask.id, timeInMillis))
                } else if (!currentState.isReminderEnabled) {
                    _cancelReminderEvent.emit(currentTask.id)
                }

                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = UiText.ResourceString(R.string.error_unknown)
                    )
                }
            }
        }
    }
}