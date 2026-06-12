package com.work.todo.ui.home.task

import com.work.todo.domain.TaskCategory
import com.work.todo.ui.home.category.CategoryItem

data class HomeUiState(
    val tasksState: HomeTasksState = HomeTasksState.Empty,
    val categories: List<CategoryItem> = emptyList(),
    val selectedCategory: TaskCategory? = null
)