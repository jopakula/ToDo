package com.work.todo.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemTaskHomeBinding
import com.work.todo.ui.home.models.HomeTaskItem

class HomeTaskAdapter(
    private val tasks: List<HomeTaskItem>,
    private val onItemClick: (HomeTaskItem, Int) -> Unit,
    private val onCheckboxChange: (HomeTaskItem, Int, Boolean) -> Unit
) : RecyclerView.Adapter<HomeTaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            task: HomeTaskItem,
            position: Int,
            onClick: (HomeTaskItem, Int) -> Unit,
            onCheck: (HomeTaskItem, Int, Boolean) -> Unit,
        ) {
            with(binding) {
                tvTaskTitle.text = task.title
                tvTaskTime.text = task.time
                itemCheckbox.setOnCheckedChangeListener(null)
                itemCheckbox.isChecked = task.isDone
                itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onCheck(task, position, isChecked)
                }
                root.setOnClickListener {
                    onClick(task, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskHomeBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], position, onItemClick, onCheckboxChange)
    }

    override fun getItemCount(): Int = tasks.size
}