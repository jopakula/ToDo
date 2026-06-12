package com.work.todo.data.database


import androidx.room.Room
import com.work.todo.data.TaskRepositoryImpl
import com.work.todo.domain.TaskRepository
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

    single<TaskRepository> { TaskRepositoryImpl(taskDao = get()) }

}