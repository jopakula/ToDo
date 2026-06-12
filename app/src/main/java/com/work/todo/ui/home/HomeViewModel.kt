package com.work.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.data.utils.TaskDateTimeUtils
import com.work.todo.domain.TaskCategory
import com.work.todo.domain.TaskRepository
import com.work.todo.ui.home.task.HomeTasksState
import com.work.todo.ui.home.task.HomeUiState
import com.work.todo.ui.mapper.CategoryMapper
import com.work.todo.ui.mapper.TaskMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<TaskCategory?>(null)

    private val todayDate = TaskDateTimeUtils.getCurrentDbDate()

    val uiState: StateFlow<HomeUiState> = combine(
        taskRepository.getFlowTasksByDate(todayDate),
        _selectedCategory
    ) { tasks, selectedCat ->

        val filteredTasks =
            if (selectedCat == null) tasks else tasks.filter { it.category == selectedCat }
        val uiTasks = TaskMapper.mapToUiList(filteredTasks)
        val tasksState =
            if (uiTasks.isEmpty()) HomeTasksState.Empty else HomeTasksState.Success(uiTasks)
        val uiCategories = CategoryMapper.getUiCategories()

        HomeUiState(
            tasksState = tasksState,
            categories = uiCategories,
            selectedCategory = selectedCat
        )
    }
        .onStart {
            emit(HomeUiState(tasksState = HomeTasksState.Loading))
        }
        .catch { e ->
            emit(HomeUiState(tasksState = HomeTasksState.Error(e.message ?: "Unknown Error")))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    fun selectCategory(category: TaskCategory) {
        _selectedCategory.update { current ->
            if (current == category) null else category
        }
    }

    fun toggleTaskStatus(taskId: Int, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.updateTaskStatus(taskId, isDone)
            } catch (e: Exception) {
            }
        }
    }
}