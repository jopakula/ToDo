package com.work.todo.ui.task.editTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.todo.database.TaskCategory
import com.work.todo.database.TaskDao
import com.work.todo.database.TaskEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditTaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val _taskState = MutableStateFlow<TaskEntity?>(null)
    val taskState: StateFlow<TaskEntity?> = _taskState

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            val task = taskDao.getTaskById(taskId)
            _taskState.value = task
        }
    }

    fun updateTask(
        title: String,
        notes: String?,
        category: TaskCategory,
        date: String?,
        time: String?,
        reminder: Boolean
    ) {
        val currentTask = _taskState.value ?: return
        viewModelScope.launch {
            val updatedTask = currentTask.copy(
                title = title,
                notes = notes,
                category = category,
                date = date,
                time = time,
                reminder = reminder
            )
            taskDao.updateTask(updatedTask)
        }
    }
}