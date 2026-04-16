package com.work.todo.ui.allTasks

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.work.todo.R
import com.work.todo.databinding.FragmentAllTasksBinding

class AllTasksFragment : Fragment(R.layout.fragment_all_tasks) {

    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllTasksBinding.bind(view)

        val allTasks = listOf(
            AllTasksItem(1, "Finish Report", "Thur, 7 Sept 2023", isOverdue = true),
            AllTasksItem(5, "Submit quarterly report", "Thur, 7 Sept 2023", isOverdue = true),
            AllTasksItem(6, "Call mom", "Thur, 7 Sept 2023", isOverdue = true),
            AllTasksItem(2, "Read Book", "Thur, 7 Sept 2023", isOverdue = false),
            AllTasksItem(3, "Water Plants", "Thur, 7 Sept 2023", isOverdue = false),
            AllTasksItem(7, "Gym session", "Fri, 8 Sept 2023", isOverdue = false),
            AllTasksItem(8, "Team meeting", "Fri, 8 Sept 2023", isOverdue = false),
            AllTasksItem(17, "Weekly team sync", "Mon, 11 Sept 2023", isOverdue = false),
            AllTasksItem(18, "Client presentation", "Mon, 11 Sept 2023", isOverdue = false),
            AllTasksItem(19, "Email follow‑ups", "Mon, 11 Sept 2023", isOverdue = false)
        )


        val overdueTasks = allTasks.filter { it.isOverdue }
        val regularTasks = allTasks.filter { !it.isOverdue }

        val overdueAdapter = AllTasksAdapter(
            tasks = overdueTasks,
            onCheckChanged = { item, position, checked -> item.isDone = checked },
            onDeleteClicked = { item, position ->
                Toast.makeText(context, "Deleted: ${item.title}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvOverdueTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOverdueTasks.adapter = overdueAdapter

        val allTasksAdapter = AllTasksAdapter(
            tasks = regularTasks,
            onCheckChanged = { item, position, checked -> item.isDone = checked },
            onDeleteClicked = { item, position ->
                Toast.makeText(context, "Deleted: ${item.title}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvAllTasksList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllTasksList.adapter = allTasksAdapter

        binding.tvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}