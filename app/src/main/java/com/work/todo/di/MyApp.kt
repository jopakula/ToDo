package com.work.todo.di

import android.app.Application
import com.work.todo.database.databaseModule
import com.work.todo.ui.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(databaseModule, viewModelModule))
        }
    }
}