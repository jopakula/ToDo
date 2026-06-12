package com.work.todo.ui.calendar.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemTaskCalendarBinding

class CalendarTaskAdapter(
    private val onDeleteClick: (CalendarTaskItem) -> Unit
) : ListAdapter<CalendarTaskItem, CalendarTaskAdapter.CalendarViewHolder>(DiffCallback) {

    private companion object DiffCallback : DiffUtil.ItemCallback<CalendarTaskItem>() {
        override fun areItemsTheSame(
            oldItem: CalendarTaskItem,
            newItem: CalendarTaskItem
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CalendarTaskItem,
            newItem: CalendarTaskItem
        ): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            ItemTaskCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick)
    }

    class CalendarViewHolder(private val binding: ItemTaskCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarTaskItem, onDelete: (CalendarTaskItem) -> Unit) {
            with(binding) {
                tvDate.text = item.date
                tvTime.text = item.time
                tvTitle.text = item.title
                tvDelete.setOnClickListener { onDelete(item) }
            }
        }
    }
}