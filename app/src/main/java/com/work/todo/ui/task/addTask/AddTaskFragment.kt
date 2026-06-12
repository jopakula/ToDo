package com.work.todo.ui.task.addTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.PorterDuff
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
import com.work.todo.R
import com.work.todo.databinding.FragmentAddTaskBinding
import com.work.todo.databinding.ItemCategoryDropdownBinding
import com.work.todo.notifications.ReminderManager
import com.work.todo.ui.mapper.CategoryMapper
import com.work.todo.ui.task.TaskUiState
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

    private fun renderUiState(state: TaskUiState) {
        binding.fab.isEnabled = !state.isLoading
        if (state.isSaved) {
            Toast.makeText(requireContext(), getString(R.string.msg_task_saved), Toast.LENGTH_SHORT)
                .show()
            findNavController().popBackStack()
            return
        }

        state.error?.let {
            Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_SHORT)
                .show()
        }

        state.selectedCategory?.let {
            binding.tvSelectedCategory.text = it.title.asString(requireContext())
        }

        binding.llCategoryOptions.visibility =
            if (state.isCategoryMenuExpanded) View.VISIBLE else View.GONE
        binding.ivCategoryArrow.animate().rotation(if (state.isCategoryMenuExpanded) 180f else 0f)
            .start()

        if (state.isCategoryMenuExpanded) {
            renderCategoryMenu()
        }

        val context = requireContext()

        val hasDate = state.formattedDate != null
        binding.tvSetDate.text = state.formattedDate ?: getString(R.string.set_date)

        val dateTextColor = ContextCompat.getColor(
            context,
            if (hasDate) R.color.text_main else R.color.text_secondary
        )
        val dateIconColor = ContextCompat.getColor(
            context,
            if (hasDate) R.color.date_accent_yellow else R.color.icon_placeholder
        )

        binding.tvSetDate.setTextColor(dateTextColor)
        binding.ivDateIcon.setColorFilter(dateIconColor, PorterDuff.Mode.SRC_IN)

        val hasTime = state.formattedTime != null
        binding.tvSetTime.text = state.formattedTime ?: getString(R.string.set_time)

        val timeTextColor = ContextCompat.getColor(
            context,
            if (hasTime) R.color.text_main else R.color.text_secondary
        )
        val timeIconColor = ContextCompat.getColor(
            context,
            if (hasTime) R.color.time_accent_orange else R.color.icon_placeholder
        )

        binding.tvSetTime.setTextColor(timeTextColor)
        binding.ivTimeIcon.setColorFilter(timeIconColor, PorterDuff.Mode.SRC_IN)

        val reminderIconColor = ContextCompat.getColor(
            context,
            if (state.isReminderEnabled) R.color.primary_blue else R.color.icon_placeholder
        )
        val reminderTextColor = ContextCompat.getColor(
            context,
            if (state.isReminderEnabled) R.color.text_main else R.color.text_secondary
        )

        binding.ivReminderIcon.setColorFilter(reminderIconColor, PorterDuff.Mode.SRC_IN)
        binding.tvSetReminder.setTextColor(reminderTextColor)
        binding.tvSetReminder.text = if (state.isReminderEnabled) {
            getString(R.string.reminder_enabled)
        } else {
            getString(R.string.set_reminder)
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
                tvCategoryName.text = item.title.asString(root.context)
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