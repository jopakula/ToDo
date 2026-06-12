package com.work.todo.ui.allTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.domain.TaskRepository
import com.work.todo.ui.allTasks.task.AllTasksTasksState
import com.work.todo.ui.allTasks.task.AllTasksUiState
import com.work.todo.ui.mapper.AllTasksMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AllTasksViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<AllTasksUiState> = combine(
        taskRepository.getFlowAllTasks(),
        _searchQuery
    ) { tasks, query ->

        val allItems = AllTasksMapper.mapToUiList(tasks)

        val filteredItems = if (query.isEmpty()) {
            allItems
        } else {
            allItems.filter { it.title.contains(query, ignoreCase = true) }
        }

        val tasksState = if (filteredItems.isEmpty()) {
            AllTasksTasksState.Empty
        } else {
            AllTasksTasksState.Success(
                overdueTasks = filteredItems.filter { it.isOverdue },
                regularTasks = filteredItems.filter { !it.isOverdue }
            )
        }

        AllTasksUiState(tasksState = tasksState, searchQuery = query)
    }
        .onStart { emit(AllTasksUiState(tasksState = AllTasksTasksState.Loading)) }
        .catch { e ->
            emit(
                AllTasksUiState(
                    tasksState = AllTasksTasksState.Error(
                        e.message ?: "Error"
                    )
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AllTasksUiState()
        )

    fun setSearchQuery(text: String) {
        _searchQuery.value = text
    }

    fun toggleTaskStatus(id: Int, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.updateTaskStatus(id, isDone)
            } catch (e: Exception) {
            }
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val task = taskRepository.getTaskById(id)
                task?.let { taskRepository.deleteTask(it) }
            } catch (e: Exception) {
            }
        }
    }
}