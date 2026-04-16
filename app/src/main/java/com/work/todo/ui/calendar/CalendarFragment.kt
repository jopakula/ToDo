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
            CalendarTaskItem(1, "16/09/2023", "09:45 PM", "Finish Report"),
            CalendarTaskItem(2, "16/09/2023", "10:00 AM", "Water the plants"),
            CalendarTaskItem(3, "17/09/2023", "11:30 AM", "Doctor's Appointment"),
            CalendarTaskItem(4, "18/09/2023", "08:00 PM", "Gym Session")
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