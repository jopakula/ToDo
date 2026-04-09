package com.work.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.work.todo.databinding.ItemTaskBinding

class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = tasks.size

    override fun getItem(position: Int): Task = tasks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemTaskBinding
        val view: View

        if (convertView == null) {
            binding = ItemTaskBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemTaskBinding
        }

        val task = tasks[position]
        binding.tvTaskText.text = task.title
        binding.btnDelete.setOnClickListener {
            tasks.removeAt(position)
            notifyDataSetChanged()
        }
        return view
    }
}