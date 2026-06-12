package com.work.todo.ui.home.category

import com.work.todo.domain.TaskCategory
import com.work.todo.ui.UiText

data class CategoryItem(
    val title: UiText,
    val iconRes: Int,
    val colorRes: Int,
    val categoryType: TaskCategory,
    val isSelected: Boolean = false,
)