package com.work.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskCategory
import com.work.todo.database.TaskDao
import com.work.todo.ui.mapper.TaskMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val dbDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val todayDate = dbDateFormatter.format(Date())

    private val _selectedCategory = MutableStateFlow<TaskCategory?>(null)

    val homeState: StateFlow<HomeState> = combine(
        taskDao.getFlowTasksByDate(todayDate),
        _selectedCategory
    ) { entities, selectedCat ->
        val filteredEntities = if (selectedCat == null) {
            entities
        } else {
            entities.filter { it.category == selectedCat }
        }

        val uiTasks = TaskMapper.mapToUiList(filteredEntities)

        if (uiTasks.isEmpty()) HomeState.Empty else HomeState.Success(uiTasks)
    }
        .onStart {
            emit(HomeState.Loading)
        }
        .catch { e -> emit(HomeState.Error(e.message ?: "Unknown Error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeState.Empty
        )

    fun selectCategory(category: TaskCategory) {
        if (_selectedCategory.value == category) {
            _selectedCategory.value = null
        } else {
            _selectedCategory.value = category
        }
    }

    fun toggleTaskStatus(taskId: Int, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskDao.updateTaskStatus(taskId, isDone)
            } catch (e: Exception) {
            }
        }
    }
}