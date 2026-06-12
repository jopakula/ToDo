package com.work.todo.ui.home.category

import com.work.todo.domain.TaskCategory

data class CategoryItem(
    val title: String,
    val iconRes: Int,
    val colorRes: Int,
    val categoryType: TaskCategory,
)