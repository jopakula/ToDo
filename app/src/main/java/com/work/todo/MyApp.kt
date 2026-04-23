package com.work.todo

import android.app.Application
import com.work.todo.utils.BackStackTracker

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        BackStackTracker.install(this)
    }
}