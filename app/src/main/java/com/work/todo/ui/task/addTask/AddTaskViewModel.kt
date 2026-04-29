package com.work.todo.ui.task.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskCategory
import com.work.todo.database.TaskDao
import com.work.todo.database.TaskEntity
import com.work.todo.ui.task.TaskState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val _state = MutableSharedFlow<TaskState>()
    val state: SharedFlow<TaskState> = _state

    fun saveTask(
        title: String,
        notes: String?,
        category: TaskCategory,
        date: String?,
        time: String?,
        reminder: Boolean
    ) {
        val task = TaskEntity(
            title = title,
            category = category,
            date = date,
            time = time,
            notes = notes,
            reminder = reminder
        )

        viewModelScope.launch {
            _state.emit(TaskState.Loading)
            try {
                taskDao.insertTask(task)
                _state.emit(TaskState.Success)
            } catch (e: Exception) {
                _state.emit(TaskState.Error(e.message ?: "Unknown Error"))
            }
        }
    }
}