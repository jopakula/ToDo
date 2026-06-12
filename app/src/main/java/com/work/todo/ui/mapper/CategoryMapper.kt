package com.work.todo.ui.mapper

import com.work.todo.R
import com.work.todo.domain.TaskCategory
import com.work.todo.ui.UiText
import com.work.todo.ui.home.category.CategoryItem

object CategoryMapper {
    fun getUiCategories(): List<CategoryItem> {
        return TaskCategory.entries.map { category ->
            CategoryItem(
                title = when (category) {
                    TaskCategory.WORK -> UiText.ResourceString(R.string.category_work)
                    TaskCategory.PERSONAL -> UiText.ResourceString(R.string.category_personal)
                    TaskCategory.SHOPPING -> UiText.ResourceString(R.string.category_shopping)
                    TaskCategory.HEALTH -> UiText.ResourceString(R.string.category_health)
                },
                iconRes = when (category) {
                    TaskCategory.WORK -> R.drawable.ic_category_work
                    TaskCategory.PERSONAL -> R.drawable.ic_category_personal
                    TaskCategory.SHOPPING -> R.drawable.ic_category_shopping
                    TaskCategory.HEALTH -> R.drawable.ic_category_health
                },
                colorRes = when (category) {
                    TaskCategory.WORK -> R.color.blue
                    TaskCategory.PERSONAL -> R.color.orange
                    TaskCategory.SHOPPING -> R.color.yellow
                    TaskCategory.HEALTH -> R.color.red
                },
                categoryType = category
            )
        }
    }
}