package com.work.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: List<Task>,
    private val onItemClick: (Task) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.tvTaskText.text = task.title
        holder.itemView.setOnClickListener { onItemClick(task) }
        holder.binding.btnDelete.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount(): Int = tasks.size
}