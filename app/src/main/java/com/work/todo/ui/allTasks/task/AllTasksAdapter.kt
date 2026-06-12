package com.work.todo.ui.allTasks.task

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemAllTasksBinding

class AllTasksAdapter(
    private val onCheckChanged: (AllTasksItem, Boolean) -> Unit,
    private val onDeleteClicked: (AllTasksItem) -> Unit
) : ListAdapter<AllTasksItem, AllTasksAdapter.AllTasksViewHolder>(DiffCallback) {

    private companion object DiffCallback : DiffUtil.ItemCallback<AllTasksItem>() {
        override fun areItemsTheSame(oldItem: AllTasksItem, newItem: AllTasksItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AllTasksItem, newItem: AllTasksItem): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTasksViewHolder {
        val binding =
            ItemAllTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllTasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllTasksViewHolder, position: Int) {
        holder.bind(getItem(position), onCheckChanged, onDeleteClicked)
    }

    class AllTasksViewHolder(private val binding: ItemAllTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: AllTasksItem,
            onCheckChanged: (AllTasksItem, Boolean) -> Unit,
            onDeleteClicked: (AllTasksItem) -> Unit
        ) {
            with(binding) {
                tvTaskTitle.text = item.title
                tvTaskDateTime.text = item.dateTimeInfo

                tvTaskDateTime.setTextColor(
                    if (item.isOverdue) Color.RED else Color.GRAY
                )

                cbDone.setOnCheckedChangeListener(null)
                cbDone.isChecked = item.isDone

                cbDone.setOnClickListener {
                    onCheckChanged(item, cbDone.isChecked)
                }

                tvDelete.setOnClickListener { onDeleteClicked(item) }
            }
        }
    }
}