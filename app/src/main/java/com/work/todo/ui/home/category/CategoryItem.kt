package com.work.todo.ui.home.category

import com.work.todo.database.TaskCategory

data class CategoryItem(
    val title: String,
    val iconRes: Int,
    val colorRes: Int,
    val categoryType: TaskCategory,
)