package com.work.todo.database


import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<TaskDatabase> {
        Room.databaseBuilder(
            androidContext(),
            TaskDatabase::class.java,
            TaskDatabase.DATABASE_NAME
        )
            .build()
    }

    single<TaskDao> { get<TaskDatabase>().taskDao() }
}