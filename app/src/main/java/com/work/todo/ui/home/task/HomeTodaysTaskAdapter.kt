package com.work.todo.ui.home.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemTaskHomeBinding

class HomeTodaysTaskAdapter(
    private val onItemClick: (HomeTaskItem) -> Unit,
    private val onCheckboxChange: (HomeTaskItem, Boolean) -> Unit
) : RecyclerView.Adapter<HomeTodaysTaskAdapter.TaskViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<HomeTaskItem>() {
        override fun areItemsTheSame(oldItem: HomeTaskItem, newItem: HomeTaskItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: HomeTaskItem, newItem: HomeTaskItem): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<HomeTaskItem>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            ItemTaskHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = differ.currentList[position]
        holder.bind(task, onItemClick, onCheckboxChange)
    }

    override fun getItemCount(): Int = differ.currentList.size

    class TaskViewHolder(private val binding: ItemTaskHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            task: HomeTaskItem,
            onClick: (HomeTaskItem) -> Unit,
            onCheck: (HomeTaskItem, Boolean) -> Unit
        ) {
            with(binding) {
                tvTaskTitle.text = task.title
                tvTaskTime.text = task.time
                itemCheckbox.setOnCheckedChangeListener(null)
                itemCheckbox.isChecked = task.isDone
                itemCheckbox.setOnCheckedChangeListener { _, isChecked -> onCheck(task, isChecked) }
                root.setOnClickListener { onClick(task) }
            }
        }
    }
}