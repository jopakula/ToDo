package com.work.todo.ui.viewModels

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
            taskDao = get()
        )
    }
    viewModel<HomeViewModel> {
        HomeViewModel(
            taskDao = get()
        )
    }
    viewModel<EditTaskViewModel> {
        EditTaskViewModel(
            taskDao = get()
        )
    }
    viewModel<CalendarViewModel> {
        CalendarViewModel(
            taskDao = get()
        )
    }
    viewModel<AllTasksViewModel> {
        AllTasksViewModel(
            taskDao = get()
        )
    }

}