package com.work.todo.ui.allTasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskDao
import com.work.todo.ui.mapper.AllTasksMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AllTasksViewModel(private val taskDao: TaskDao) : ViewModel() {

    val uiState: StateFlow<AllTasksState> = taskDao.getFlowAllTasks()
        .map { entities ->
            val allItems = AllTasksMapper.mapToUiList(entities)

            if (allItems.isEmpty()) {
                AllTasksState.Empty
            } else {
                AllTasksState.Success(
                    overdueTasks = allItems.filter { it.isOverdue },
                    regularTasks = allItems.filter { !it.isOverdue }
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