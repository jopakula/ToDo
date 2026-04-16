package com.work.todo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.work.todo.R
import com.work.todo.databinding.FragmentHomeBinding
import com.work.todo.ui.home.category.HomeCategoryAdapter
import com.work.todo.ui.home.category.HomeCategoryItem
import com.work.todo.ui.home.task.HomeTaskItem
import com.work.todo.ui.home.task.HomeTodaysTaskAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        val categories = listOf(
            HomeCategoryItem(1, "Work", R.drawable.ic_category_work, R.color.blue),
            HomeCategoryItem(2, "Personal", R.drawable.ic_category_personal, R.color.orange),
            HomeCategoryItem(3, "Shopping", R.drawable.ic_category_shopping, R.color.yellow),
            HomeCategoryItem(4, "Health", R.drawable.ic_category_health, R.color.red)
        )

        val dummyTasks = listOf(
            HomeTaskItem(1, "Finish Report", "10:00 am", true),
            HomeTaskItem(2, "Gym Workout", "12:00 pm", false),
            HomeTaskItem(3, "Project Meeting", "02:00 pm", false),
            HomeTaskItem(4, "Read Chapter 3", "04:00 pm", false),
            HomeTaskItem(5, "Cook Dinner", "07:00 pm", false),
            HomeTaskItem(6, "Call Mom", "09:00 am", true),
            HomeTaskItem(7, "Buy Groceries", "01:30 pm", false),
            HomeTaskItem(8, "Review Code", "03:45 pm", false),
            HomeTaskItem(9, "Watch Tutorial", "06:15 pm", false),
            HomeTaskItem(10, "Reply to Emails", "08:30 pm", false)
        )

        val categoryAdapter = HomeCategoryAdapter(categories) { item, position ->
            Toast.makeText(requireContext(), "Category: ${item.title}", Toast.LENGTH_SHORT).show()
        }


        adapter = HomeTodaysTaskAdapter(
            tasks = dummyTasks,
            onItemClick = { task, position ->
                task.isDone = !task.isDone
                adapter.notifyItemChanged(position)
                Toast.makeText(
                    requireContext(),
                    "Нажат элемент №$position: ${task.title}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onCheckboxChange = { task, position, isChecked ->
                task.isDone = isChecked
                Toast.makeText(
                    requireContext(),
                    "Элемент $position: ${task.title} теперь имеет статус: $isChecked",
                    Toast.LENGTH_SHORT
                ).show()
            })

        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCategories.adapter = categoryAdapter

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter

        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_allTasksFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}