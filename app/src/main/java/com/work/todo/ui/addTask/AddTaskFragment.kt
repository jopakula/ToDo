package com.work.todo.ui.addTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.todo.database.TaskCategory
import com.work.todo.database.TaskDao
import com.work.todo.database.TaskEntity
import com.work.todo.databinding.FragmentAddTaskBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val taskDao: TaskDao by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        binding.fabDone.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = binding.etTaskName.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()

        val date = "2024-05-20"
        val time = "12:00"
        val category = TaskCategory.WORK

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Введите название задачи", Toast.LENGTH_SHORT).show()
            return
        }

        val task = TaskEntity(
            title = title,
            category = category,
            date = date,
            time = time,
            notes = notes,
            isDone = false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                taskDao.insertTask(task)
                Toast.makeText(requireContext(), "Задача сохранена!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupUI() {
        binding.fabDone.scaleX = 0f
        binding.fabDone.scaleY = 0f
        binding.fabDone.postDelayed({ animateFab() }, 300)

        binding.tvCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun animateFab() {
        binding.fabDone.visibility = View.VISIBLE
        binding.fabDone.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}