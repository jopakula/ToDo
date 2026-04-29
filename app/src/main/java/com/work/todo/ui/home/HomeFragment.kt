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
import com.work.todo.ui.home.task.HomeTodaysTaskAdapter
import com.work.todo.ui.mapper.CategoryMapper
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var adapter: HomeTodaysTaskAdapter

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
        val categories = CategoryMapper.getUiCategories()
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCategories.adapter = HomeCategoryAdapter(categories) { item, _ ->
            viewModel.selectCategory(item.categoryType)
        }

        adapter = HomeTodaysTaskAdapter(
            onItemClick = { task ->
                val action = HomeFragmentDirections.actionHomeToEditTask(task.id)
                findNavController().navigate(action)
            },
            onCheckboxChange = { task, isChecked ->
                viewModel.toggleTaskStatus(task.id, isChecked)
            }
        )

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeState.collect { state ->
                    when (state) {
                        is HomeState.Empty -> {
                            binding.rvTasks.visibility = View.GONE
                            binding.llNoTasks.visibility = View.VISIBLE
                        }

                        is HomeState.Loading -> {
                            binding.rvTasks.visibility = View.GONE
                            binding.llNoTasks.visibility = View.GONE
                        }

                        is HomeState.Success -> {
                            binding.rvTasks.visibility = View.VISIBLE
                            binding.llNoTasks.visibility = View.GONE
                            adapter.submitList(state.tasks)
                        }

                        is HomeState.Error -> {
                            binding.llNoTasks.visibility = View.GONE
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