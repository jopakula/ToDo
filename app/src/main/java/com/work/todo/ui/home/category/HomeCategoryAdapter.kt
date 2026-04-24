package com.work.todo.ui.home.category

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.HomeItemCategoryBinding

class HomeCategoryAdapter(
    private val items: List<CategoryItem>,
    private val onItemClick: (CategoryItem, Int) -> Unit,
) :
    RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: HomeItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryItem, position: Int, onClick: (CategoryItem, Int) -> Unit) {
            with(binding) {
                tvCatTitle.text = item.title
                ivCatIcon.setImageResource(item.iconRes)
                val color =
                    ContextCompat.getColor(root.context, item.colorRes)
                tvCatTitle.setTextColor(color)
                ivCatIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
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