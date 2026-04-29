package com.work.todo.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.ItemTaskCalendarBinding

class CalendarTaskAdapter(
    private val onDeleteClick: (CalendarTaskItem) -> Unit
) : RecyclerView.Adapter<CalendarTaskAdapter.CalendarViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<CalendarTaskItem>() {
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

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<CalendarTaskItem>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            ItemTaskCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(differ.currentList[position], onDeleteClick)
    }

    override fun getItemCount() = differ.currentList.size

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