package com.work.todo.ui.calendar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.work.todo.R
import com.work.todo.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCalendarBinding.bind(view)

        val calendarTasks = listOf(
            CalendarTaskItem(1, "16/09/2023", "07:30 AM", "Wake up and morning routine"),
            CalendarTaskItem(2, "16/09/2023", "08:00 AM", "Breakfast"),
            CalendarTaskItem(3, "16/09/2023", "09:00 AM", "Team meeting"),
            CalendarTaskItem(17, "17/09/2023", "01:00 PM", "Lunch with colleague"),
            CalendarTaskItem(18, "17/09/2023", "02:30 PM", "Code review"),
            CalendarTaskItem(28, "18/09/2023", "04:30 PM", "Performance review"),
            CalendarTaskItem(29, "18/09/2023", "06:15 PM", "Tennis practice"),
            CalendarTaskItem(30, "18/09/2023", "08:00 PM", "Gym Session"),
            CalendarTaskItem(31, "18/09/2023", "09:30 PM", "Evening walk"),
            CalendarTaskItem(36, "19/09/2023", "01:15 PM", "Lunch")
        )


        val calendarAdapter = CalendarTaskAdapter(calendarTasks) { task, position ->
            Toast.makeText(context, "Удаляем: ${task.title} элемент №$position", Toast.LENGTH_SHORT)
                .show()
        }

        binding.rvCalendarTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCalendarTasks.adapter = calendarAdapter

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(context, "Выбрано: $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}