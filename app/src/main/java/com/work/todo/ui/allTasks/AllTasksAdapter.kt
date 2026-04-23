package com.work.todo.ui.allTasks

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemAllTasksBinding

class AllTasksAdapter(
    private val tasks: List<AllTasksItem>,
    private val onCheckChanged: (AllTasksItem, Int, Boolean) -> Unit,
    private val onDeleteClicked: (AllTasksItem, Int) -> Unit
) : RecyclerView.Adapter<AllTasksAdapter.AllTasksViewHolder>() {

    class AllTasksViewHolder(val binding: ItemAllTasksBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTasksViewHolder {
        val binding = ItemAllTasksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AllTasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllTasksViewHolder, position: Int) {
        val item = tasks[position]
        val context = holder.itemView.context

        with(holder.binding) {
            tvTaskTitle.text = item.title
            tvTaskDateTime.text = item.dateTimeInfo
            if (item.isOverdue && !item.isDone) {
                tvTaskDateTime.setTextColor(ContextCompat.getColor(context, R.color.holo_red_light))
            } else {
                tvTaskDateTime.setTextColor(ContextCompat.getColor(context, R.color.darker_gray))
            }

            cbDone.setOnCheckedChangeListener(null)
            cbDone.isChecked = item.isDone
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(item, position, isChecked)
            }

            tvDelete.setOnClickListener {
                onDeleteClicked(item, position)
            }
        }
    }

    override fun getItemCount() = tasks.size
}