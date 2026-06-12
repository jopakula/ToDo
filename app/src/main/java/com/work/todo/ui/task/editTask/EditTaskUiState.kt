package com.work.todo.ui.task.editTask

import com.work.todo.ui.home.category.CategoryItem

data class EditTaskUiState(
    val title: String = "",
    val notes: String = "",
    val selectedCategory: CategoryItem? = null,
    val isCategoryMenuExpanded: Boolean = false,
    val formattedDate: String = "Set Date",
    val formattedTime: String = "Set Time",
    val isReminderEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val rawDate: String? = null,
    val rawTime: String? = null
)