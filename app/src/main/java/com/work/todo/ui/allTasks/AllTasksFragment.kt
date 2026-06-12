package com.work.todo.ui.allTasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.work.todo.databinding.FragmentAllTasksBinding
import com.work.todo.ui.allTasks.task.AllTasksAdapter
import com.work.todo.ui.allTasks.task.AllTasksTasksState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllTasksFragment : Fragment() {

    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllTasksViewModel by viewModel()
    private lateinit var overdueAdapter: AllTasksAdapter
    private lateinit var regularAdapter: AllTasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        observeViewModel()
        setupListeners()
    }

    private fun setupListeners() {
        binding.etSearchAll.doOnTextChanged { text, _, _, _ ->
            viewModel.setSearchQuery(text.toString())
        }

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

        binding.rvOverdueTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOverdueTasks.adapter = overdueAdapter

        binding.rvAllTasksList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllTasksList.adapter = regularAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    when (val tasksState = state.tasksState) {
                        is AllTasksTasksState.Loading -> {}

                        is AllTasksTasksState.Success -> {
                            overdueAdapter.submitList(tasksState.overdueTasks)
                            regularAdapter.submitList(tasksState.regularTasks)

                            binding.tvOverdueLabel.visibility =
                                if (tasksState.overdueTasks.isEmpty()) View.GONE else View.VISIBLE
                            binding.tvAllTasksLabel.visibility =
                                if (tasksState.regularTasks.isEmpty()) View.GONE else View.VISIBLE
                        }

                        is AllTasksTasksState.Empty -> {
                            overdueAdapter.submitList(emptyList())
                            regularAdapter.submitList(emptyList())
                            binding.tvOverdueLabel.visibility = View.GONE
                            binding.tvAllTasksLabel.visibility = View.GONE
                        }

                        is AllTasksTasksState.Error -> {
                            Toast.makeText(requireContext(), tasksState.message, Toast.LENGTH_SHORT)
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