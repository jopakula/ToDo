package com.work.todo.ui.allTasks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemAllTasksBinding

class AllTasksAdapter(
    private val onCheckChanged: (AllTasksItem, Boolean) -> Unit,
    private val onDeleteClicked: (AllTasksItem) -> Unit
) : RecyclerView.Adapter<AllTasksAdapter.AllTasksViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<AllTasksItem>() {
        override fun areItemsTheSame(oldItem: AllTasksItem, newItem: AllTasksItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AllTasksItem, newItem: AllTasksItem) =
            oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<AllTasksItem>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTasksViewHolder {
        val binding =
            ItemAllTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllTasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllTasksViewHolder, position: Int) {
        val item = differ.currentList[position]
        with(holder.binding) {
            tvTaskTitle.text = item.title
            tvTaskDateTime.text = item.dateTimeInfo

            tvTaskDateTime.setTextColor(
                if (item.isOverdue) Color.RED else Color.GRAY
            )

            cbDone.setOnCheckedChangeListener(null)
            cbDone.isChecked = item.isDone
            cbDone.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(item, isChecked) }

            tvDelete.setOnClickListener { onDeleteClicked(item) }
        }
    }

    override fun getItemCount() = differ.currentList.size
    class AllTasksViewHolder(val binding: ItemAllTasksBinding) :
        RecyclerView.ViewHolder(binding.root)
}