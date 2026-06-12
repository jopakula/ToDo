package com.work.todo.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCategory(category: TaskEntityCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): TaskEntityCategory {
        return TaskEntityCategory.valueOf(value)
    }
}