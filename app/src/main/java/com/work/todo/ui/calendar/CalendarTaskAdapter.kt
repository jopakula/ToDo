package com.work.todo.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemTaskCalendarBinding

class CalendarTaskAdapter(
    private val tasks: List<CalendarTaskItem>,
    private val onDeleteClick: (CalendarTaskItem, Int) -> Unit
) : RecyclerView.Adapter<CalendarTaskAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(val binding: ItemTaskCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarTaskItem, position: Int, onClick: (CalendarTaskItem, Int) -> Unit) {
            with(binding) {
                tvDate.text = item.date
                tvTime.text = item.time
                tvTitle.text = item.title
                tvDelete.setOnClickListener { onClick(item, position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskCalendarBinding.inflate(inflater, parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(tasks[position], position, onDeleteClick)
    }

    override fun getItemCount() = tasks.size
}