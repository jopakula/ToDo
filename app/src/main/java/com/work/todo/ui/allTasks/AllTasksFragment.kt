package com.work.todo.ui.allTasks

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.work.todo.R
import com.work.todo.databinding.FragmentAllTasksBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllTasksFragment : Fragment(R.layout.fragment_all_tasks) {

    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllTasksViewModel by viewModel()
    private lateinit var overdueAdapter: AllTasksAdapter
    private lateinit var regularAdapter: AllTasksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllTasksBinding.bind(view)

        setupAdapters()
        observeViewModel()

        binding.tvBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setupAdapters() {
        overdueAdapter = AllTasksAdapter(
            onCheckChanged = { item, checked -> viewModel.toggleTaskStatus(item.id, checked) },
            onDeleteClicked = { item -> viewModel.deleteTask(item.id) }
        )

        regularAdapter = AllTasksAdapter(
            onCheckChanged = { item, checked -> viewModel.toggleTaskStatus(item.id, checked) },
            onDeleteClicked = { item -> viewModel.deleteTask(item.id) }
        )

        binding.rvOverdueTasks.adapter = overdueAdapter
        binding.rvAllTasksList.adapter = regularAdapter
        binding.rvAllTasksList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOverdueTasks.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is AllTasksState.Loading -> {}
                        is AllTasksState.Success -> {
                            overdueAdapter.submitList(state.overdueTasks)
                            regularAdapter.submitList(state.regularTasks)
                        }

                        is AllTasksState.Empty -> {
                            overdueAdapter.submitList(emptyList())
                            regularAdapter.submitList(emptyList())
                        }

                        is AllTasksState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
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