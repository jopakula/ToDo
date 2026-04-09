package com.work.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.work.todo.databinding.FragmentListBinding


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val tasks = mutableListOf(
            Task("Купить молоко", "Нужно 2 литра жирность 3.2%"),
            Task("Сделать проект", "Допилить навигацию и списки"),
            Task("Покормить кота", "А то меня он сожрет"),
        )

        val taskAdapter = TaskAdapter(
            tasks = tasks,
            onItemClick = { selectedTask ->
                val bundle = Bundle().apply { putSerializable("task", selectedTask) }
                findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle)
            },
            onDeleteClick = { position ->
                tasks.removeAt(position)
                binding.recyclerView.adapter?.notifyItemRemoved(position)
            }
        )

        binding.recyclerView.adapter = taskAdapter

        binding.buttonAdd.setOnClickListener {
            val title = binding.editTextTask.text.toString()
            val description = binding.editTextDescription.text.toString()

            if (title.isNotBlank()) {

                val finalDescription = description.ifBlank { "No description" }
                val newTask = Task(title, finalDescription)

                tasks.add(0, newTask)

                taskAdapter.notifyItemInserted(0)

                binding.editTextTask.text.clear()
                binding.editTextDescription.text.clear()

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}