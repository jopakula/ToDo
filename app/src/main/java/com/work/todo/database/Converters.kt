package com.work.todo.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCategory(category: TaskCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): TaskCategory {
        return TaskCategory.valueOf(value)
    }
}