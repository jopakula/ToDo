package com.work.todo.ui.allTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskDao
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

class AllTasksViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<AllTasksState> = combine(
        taskDao.getFlowAllTasks(),
        _searchQuery
    ) { entities, query ->

        val allItems = AllTasksMapper.mapToUiList(entities)

        val filteredItems = if (query.isEmpty()) {
            allItems
        } else {
            allItems.filter { it.title.contains(query, ignoreCase = true) }
        }

        if (filteredItems.isEmpty()) {
            AllTasksState.Empty
        } else {
            AllTasksState.Success(
                overdueTasks = filteredItems.filter { it.isOverdue },
                regularTasks = filteredItems.filter { !it.isOverdue }
            )
        }
    }
        .onStart { emit(AllTasksState.Loading) }
        .catch { e -> emit(AllTasksState.Error(e.message ?: "Error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AllTasksState.Loading
        )

    fun setSearchQuery(text: String) {
        _searchQuery.value = text
    }

    fun toggleTaskStatus(id: Int, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.updateTaskStatus(id, isDone)
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = taskDao.getTaskById(id)
            task?.let { taskDao.deleteTask(it) }
        }
    }
}