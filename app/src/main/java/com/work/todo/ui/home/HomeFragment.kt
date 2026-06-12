package com.work.todo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.work.todo.R
import com.work.todo.databinding.FragmentHomeBinding
import com.work.todo.ui.home.category.HomeCategoryAdapter
import com.work.todo.ui.home.task.HomeTasksState
import com.work.todo.ui.home.task.HomeTodaysTaskAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var tasksAdapter: HomeTodaysTaskAdapter
    private lateinit var categoriesAdapter: HomeCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupObservers()

        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_allTasks)
        }
    }

    private fun setupRecyclerViews() {
        categoriesAdapter = HomeCategoryAdapter { item ->
            viewModel.selectCategory(item.categoryType)
        }
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCategories.adapter = categoriesAdapter

        tasksAdapter = HomeTodaysTaskAdapter(
            onItemClick = { task ->
                val action = HomeFragmentDirections.actionHomeToEditTask(task.id)
                findNavController().navigate(action)
            },
            onCheckboxChange = { task, isChecked ->
                viewModel.toggleTaskStatus(task.id, isChecked)
            }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = tasksAdapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    categoriesAdapter.updateData(state.categories, state.selectedCategory)

                    when (state.tasksState) {
                        is HomeTasksState.Empty -> {
                            binding.rvTasks.visibility = View.GONE
                            binding.llNoTasks.visibility = View.VISIBLE
                        }

                        is HomeTasksState.Loading -> {
                            binding.rvTasks.visibility = View.GONE
                            binding.llNoTasks.visibility = View.GONE
                        }

                        is HomeTasksState.Success -> {
                            binding.rvTasks.visibility = View.VISIBLE
                            binding.llNoTasks.visibility = View.GONE
                            tasksAdapter.submitList(state.tasksState.tasks)
                        }

                        is HomeTasksState.Error -> {
                            binding.llNoTasks.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                state.tasksState.message,
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