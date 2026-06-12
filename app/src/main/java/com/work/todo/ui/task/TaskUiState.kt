package com.work.todo.ui.task

import com.work.todo.ui.UiText
import com.work.todo.ui.home.category.CategoryItem

data class TaskUiState(
    val title: String = "",
    val notes: String = "",
    val selectedCategory: CategoryItem? = null,
    val isCategoryMenuExpanded: Boolean = false,
    val formattedDate: String? = null,
    val formattedTime: String? = null,
    val isReminderEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: UiText? = null,
    val rawDate: String? = null,
    val rawTime: String? = null
)