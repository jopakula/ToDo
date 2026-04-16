package com.work.todo.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.HomeItemCategoryBinding
import com.work.todo.ui.home.models.HomeCategoryItem

class HomeCategoryAdapter(
    private val items: List<HomeCategoryItem>,
    private val onItemClick: (HomeCategoryItem, Int) -> Unit,
) :
    RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: HomeItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeCategoryItem, position: Int, onClick: (HomeCategoryItem, Int) -> Unit) {
            with(binding) {
                tvCatTitle.text = item.title
                ivCatIcon.setImageResource(item.iconRes)
                val color =
                    androidx.core.content.ContextCompat.getColor(root.context, item.colorRes)
                tvCatTitle.setTextColor(color)
                ivCatIcon.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
                root.setOnClickListener { onClick(item, position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position], position, onItemClick)
    }

    override fun getItemCount() = items.size
}