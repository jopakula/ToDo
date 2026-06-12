package com.work.todo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.work.todo.databinding.FragmentCalendarBinding
import com.work.todo.ui.calendar.task.CalendarTaskAdapter
import com.work.todo.ui.calendar.task.CalendarTasksState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModel()
    private lateinit var calendarAdapter: CalendarTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCalendar()
        observeState()
    }

    private fun setupRecyclerView() {
        calendarAdapter = CalendarTaskAdapter { task ->
            viewModel.deleteTask(task.id)
        }

        binding.rvCalendarTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = calendarAdapter
        }
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.selectDate(year, month, dayOfMonth)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    when (state.tasksState) {
                        is CalendarTasksState.Loading -> {
                            binding.rvCalendarTasks.visibility = View.GONE
                            calendarAdapter.submitList(emptyList())
                        }

                        is CalendarTasksState.Success -> {
                            binding.rvCalendarTasks.visibility = View.VISIBLE
                            calendarAdapter.submitList(state.tasksState.tasks)
                        }

                        is CalendarTasksState.Empty -> {
                            binding.rvCalendarTasks.visibility = View.GONE
                            calendarAdapter.submitList(emptyList())
                        }

                        is CalendarTasksState.Error -> {
                            binding.rvCalendarTasks.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                state.tasksState.message.asString(requireContext()),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}