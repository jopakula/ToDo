package com.work.todo.ui

import com.work.todo.ui.allTasks.AllTasksViewModel
import com.work.todo.ui.calendar.CalendarViewModel
import com.work.todo.ui.home.HomeViewModel
import com.work.todo.ui.task.addTask.AddTaskViewModel
import com.work.todo.ui.task.editTask.EditTaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<AddTaskViewModel> {
        AddTaskViewModel(
            taskRepository = get()
        )
    }
    viewModel<HomeViewModel> {
        HomeViewModel(
            taskRepository = get()
        )
    }
    viewModel<EditTaskViewModel> {
        EditTaskViewModel(
            taskRepository = get()
        )
    }
    viewModel<CalendarViewModel> {
        CalendarViewModel(
            taskRepository = get()
        )
    }
    viewModel<AllTasksViewModel> {
        AllTasksViewModel(
            taskRepository = get()
        )
    }

}