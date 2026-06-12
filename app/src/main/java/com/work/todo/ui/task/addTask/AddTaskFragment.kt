package com.work.todo.ui.task.addTask

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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.work.todo.databinding.FragmentAddTaskBinding
import com.work.todo.databinding.ItemCategoryDropdownBinding
import com.work.todo.notifications.ReminderManager
import com.work.todo.ui.mapper.CategoryMapper
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var reminderManager: ReminderManager
    private val viewModel: AddTaskViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reminderManager = ReminderManager(requireContext())

        setupListeners()
        setupObservers()
        setupFabAnimation()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiState.collect { state ->
                        renderUiState(state)
                    }
                }

                launch {
                    viewModel.scheduleReminderEvent.collect { (taskId, timeInMillis) ->
                        reminderManager.setReminder(
                            taskId = taskId,
                            title = binding.etTaskName.text.toString().trim(),
                            triggerTimeMs = timeInMillis
                        )
                    }
                }
            }
        }
    }

    private fun renderUiState(state: AddTaskUiState) {

        binding.fab.isEnabled = !state.isLoading
        if (state.isSaved) {
            Toast.makeText(requireContext(), "Задача сохранена!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        state.error?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        state.selectedCategory?.let {
            binding.tvSelectedCategory.text = it.title
        }

        binding.llCategoryOptions.visibility =
            if (state.isCategoryMenuExpanded) View.VISIBLE else View.GONE
        binding.ivCategoryArrow.animate().rotation(if (state.isCategoryMenuExpanded) 180f else 0f)
            .start()
        if (state.isCategoryMenuExpanded) {
            renderCategoryMenu()
        }

        binding.tvSetDate.text = state.formattedDate
        binding.tvSetTime.text = state.formattedTime

        binding.tvSetDate.setTextColor(if (state.formattedDate == "Set Date") Color.GRAY else Color.BLACK)
        binding.ivDateIcon.setColorFilter(
            if (state.formattedDate == "Set Date") Color.parseColor("#8E8E8E") else Color.parseColor(
                "#FFC107"
            )
        )

        binding.tvSetTime.setTextColor(if (state.formattedTime == "Set Time") Color.GRAY else Color.BLACK)
        binding.ivTimeIcon.setColorFilter(
            if (state.formattedTime == "Set Time") Color.parseColor("#8E8E8E") else Color.parseColor(
                "#FF5722"
            )
        )

        if (state.isReminderEnabled) {
            binding.ivReminderIcon.setColorFilter(Color.parseColor("#4A90E2"))
            binding.tvSetReminder.text = "Reminder Enabled"
            binding.tvSetReminder.setTextColor(Color.BLACK)
        } else {
            binding.ivReminderIcon.setColorFilter(Color.parseColor("#8E8E8E"))
            binding.tvSetReminder.text = "Set Reminder"
            binding.tvSetReminder.setTextColor(Color.GRAY)
        }
    }

    private fun setupListeners() {
        binding.etTaskName.doAfterTextChanged { viewModel.onTitleChanged(it?.toString().orEmpty()) }
        binding.etNotes.doAfterTextChanged { viewModel.onNotesChanged(it?.toString().orEmpty()) }

        binding.btnSelectCategory.setOnClickListener { viewModel.toggleCategoryMenu() }
        binding.fab.setOnClickListener { viewModel.saveTask() }
        binding.tvCancel.setOnClickListener { findNavController().popBackStack() }
        binding.llSetDate.setOnClickListener { showDatePicker() }
        binding.llSetTime.setOnClickListener { showTimePicker() }
        binding.llSetReminder.setOnClickListener { viewModel.toggleReminder() }
    }

    private fun renderCategoryMenu() {
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
                root.setOnClickListener { viewModel.selectCategory(item) }
            }
            container.addView(itemBinding.root)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, y, m, d -> viewModel.onDateSelected(y, m, d) },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(), { _, h, m -> viewModel.onTimeSelected(h, m) },
            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        ).show()
    }

    private fun setupFabAnimation() {
        binding.fab.apply {
            scaleX = 0f; scaleY = 0f; alpha = 0f
            postDelayed({
                visibility = View.VISIBLE
                animate().scaleX(1f).scaleY(1f).alpha(1f)
                    .setDuration(500).setInterpolator(OvershootInterpolator()).start()
            }, 300)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}