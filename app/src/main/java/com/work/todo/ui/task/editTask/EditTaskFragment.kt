package com.work.todo.ui.task.editTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.work.todo.database.TaskCategory
import com.work.todo.databinding.FragmentEditTaskBinding
import com.work.todo.databinding.ItemCategoryDropdownBinding
import com.work.todo.ui.mapper.CategoryMapper
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val args: EditTaskFragmentArgs by navArgs()
    private val viewModel: EditTaskViewModel by viewModel()

    private var selectedCategory: TaskCategory = TaskCategory.WORK
    private var isMenuExpanded = false
    private var isReminderActivated = false

    private var selectedDateMillis: Long? = null
    private var selectedTimeMillis: Long? = null

    private val dateFormatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dbDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadTask(args.taskId)

        setupObservers()
        setupListeners()
        setupFab()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskState.collect { task ->
                    task?.let { entity ->
                        binding.etTaskName.setText(entity.title)
                        binding.etNotes.setText(entity.notes)
                        selectedCategory = entity.category
                        binding.tvSelectedCategory.text = entity.category.name.lowercase()
                            .replaceFirstChar { it.uppercase() }

                        entity.date?.let { dateStr ->
                            val date = dbDateFormatter.parse(dateStr)
                            date?.let {
                                selectedDateMillis = it.time
                                binding.tvSetDate.text = dateFormatter.format(it)
                                binding.tvSetDate.setTextColor(Color.BLACK)
                                binding.ivDateIcon.setColorFilter(Color.parseColor("#FFC107"))
                            }
                        }

                        entity.time?.let { timeStr ->
                            val time = timeFormatter.parse(timeStr)
                            time?.let {
                                selectedTimeMillis = it.time
                                binding.tvSetTime.text = timeFormatter.format(it)
                                binding.tvSetTime.setTextColor(Color.BLACK)
                                binding.ivTimeIcon.setColorFilter(Color.parseColor("#FF5722"))
                            }
                        }

                        isReminderActivated = entity.reminder
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.tvCancel.setOnClickListener { findNavController().popBackStack() }
        binding.btnSelectCategory.setOnClickListener { toggleMenu() }
        binding.llSetDate.setOnClickListener { showDatePicker() }
        binding.llSetTime.setOnClickListener { showTimePicker() }
        binding.llSetReminder.setOnClickListener { updateReminderUi() }

        binding.fab.setOnClickListener {
            validateAndSave()
        }
    }

    private fun updateReminderUi() {
        isReminderActivated = !isReminderActivated
        val tvReminder = binding.tvSetReminder
        if (isReminderActivated) {
            binding.ivReminderIcon.setColorFilter(Color.parseColor("#4A90E2"))
            tvReminder.text = "Reminder Enabled"
            tvReminder.setTextColor(Color.BLACK)
        } else {
            binding.ivReminderIcon.setColorFilter(Color.parseColor("#8E8E8E"))
            tvReminder.text = "Set Reminder"
            tvReminder.setTextColor(Color.GRAY)
        }
    }

    private fun validateAndSave() {
        val title = binding.etTaskName.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Введите название задачи", Toast.LENGTH_SHORT).show()
            return
        }

        val dateString = selectedDateMillis?.let { dbDateFormatter.format(Date(it)) }
        val timeString = selectedTimeMillis?.let { timeFormatter.format(Date(it)) }

        viewModel.updateTask(
            title = title,
            notes = notes.ifEmpty { null },
            category = selectedCategory,
            date = dateString,
            time = timeString,
            reminder = isReminderActivated
        )

        Toast.makeText(requireContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        selectedDateMillis?.let { calendar.timeInMillis = it }

        DatePickerDialog(
            requireContext(), { _, y, m, d ->
                val pickedCal = Calendar.getInstance()
                pickedCal.set(y, m, d)
                selectedDateMillis = pickedCal.timeInMillis

                binding.tvSetDate.text = dateFormatter.format(pickedCal.time)
                binding.tvSetDate.setTextColor(Color.BLACK)
                binding.ivDateIcon.setColorFilter(Color.parseColor("#FFC107"))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        selectedTimeMillis?.let { calendar.timeInMillis = it }

        TimePickerDialog(
            requireContext(), { _, h, m ->
                val pickedCal = Calendar.getInstance()
                pickedCal.set(Calendar.HOUR_OF_DAY, h)
                pickedCal.set(Calendar.MINUTE, m)
                selectedTimeMillis = pickedCal.timeInMillis

                binding.tvSetTime.text = timeFormatter.format(pickedCal.time)
                binding.tvSetTime.setTextColor(Color.BLACK)
                binding.ivTimeIcon.setColorFilter(Color.parseColor("#FF5722"))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), true
        ).show()
    }

    private fun toggleMenu() {
        if (!isMenuExpanded) setupCategoryMenu()
        isMenuExpanded = !isMenuExpanded
        binding.ivCategoryArrow.animate().rotation(if (isMenuExpanded) 180f else 0f).start()
        binding.llCategoryOptions.visibility = if (isMenuExpanded) View.VISIBLE else View.GONE
    }

    private fun setupCategoryMenu() {
        val container = binding.llCategoryOptions
        container.removeAllViews()
        CategoryMapper.getUiCategories().forEach { item ->
            val itemBinding = ItemCategoryDropdownBinding.inflate(layoutInflater, container, false)
            with(itemBinding) {
                tvCategoryName.text = item.title
                ivCategoryIcon.setImageResource(item.iconRes)
                ivCategoryIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        item.colorRes
                    )
                )
                root.setOnClickListener {
                    selectedCategory = item.categoryType
                    binding.tvSelectedCategory.text = item.title
                    toggleMenu()
                }
            }
            container.addView(itemBinding.root)
        }
    }

    private fun setupFab() {
        binding.fab.apply {
            scaleX = 0f
            scaleY = 0f
            alpha = 0f
            postDelayed({ animateFab() }, 300)
        }
    }

    private fun animateFab() {
        binding.fab.apply {
            visibility = View.VISIBLE
            animate()
                .scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(500)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}