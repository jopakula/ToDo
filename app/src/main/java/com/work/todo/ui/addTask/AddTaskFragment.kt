package com.work.todo.ui.addTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.work.todo.R
import com.work.todo.database.TaskCategory
import com.work.todo.database.TaskDao
import com.work.todo.database.TaskEntity
import com.work.todo.databinding.FragmentAddTaskBinding
import com.work.todo.databinding.ItemCategoryDropdownBinding
import com.work.todo.ui.home.category.CategoryItem
import com.work.todo.ui.mapper.CategoryMapper
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: TaskCategory = TaskCategory.WORK
    private var isMenuExpanded = false

    private val taskDao: TaskDao by inject()

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: Calendar = Calendar.getInstance()

    private val dateFormatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val dbDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSetDate.setOnClickListener { showDatePicker() }
        binding.tvSetTime.setOnClickListener { showTimePicker() }

        setupUI()

        binding.btnSelectCategory.setOnClickListener {
            setupCategoryMenu()
        }

        binding.fabDone.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = binding.etTaskName.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()


        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Введите название задачи", Toast.LENGTH_SHORT).show()
            return
        }

        val task = TaskEntity(
            title = title,
            category = selectedCategory,
            date = dbDateFormatter.format(selectedDate.time),
            time = timeFormatter.format(selectedTime.time),
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

    private fun setupCategoryMenu() {
        val categories = CategoryMapper.getUiCategories()
        val container = binding.llCategoryOptions

        container.removeAllViews()

        categories.forEach { item ->
            val itemBinding = ItemCategoryDropdownBinding.inflate(
                layoutInflater,
                container,
                false
            )

            itemBinding.tvCategoryName.text = item.title
            itemBinding.ivCategoryIcon.setImageResource(item.iconRes)
            itemBinding.ivCategoryIcon.setColorFilter(
                ContextCompat.getColor(requireContext(), item.colorRes)
            )

            itemBinding.root.setOnClickListener {
                selectCategory(item)
                toggleMenu()
            }

            container.addView(itemBinding.root)
        }

        binding.btnSelectCategory.setOnClickListener {
            toggleMenu()
        }
    }

    private fun selectCategory(item: CategoryItem) {
        selectedCategory = item.categoryType
        binding.tvSelectedCategory.text = item.title
    }

    private fun toggleMenu() {
        isMenuExpanded = !isMenuExpanded
        binding.ivCategoryArrow.animate().rotation(if (isMenuExpanded) 180f else 0f).start()
        binding.llCategoryOptions.visibility = if (isMenuExpanded) View.VISIBLE else View.GONE
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)

                val tvDate = binding.tvSetDate.getChildAt(1) as TextView
                tvDate.text = dateFormatter.format(selectedDate.time)
                tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                val tvTime = binding.tvSetTime.getChildAt(1) as TextView
                tvTime.text = timeFormatter.format(selectedTime.time)
                tvTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            },
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}