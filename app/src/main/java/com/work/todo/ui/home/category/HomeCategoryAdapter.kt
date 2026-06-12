package com.work.todo.ui.home.category

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.work.todo.databinding.HomeItemCategoryBinding
import com.work.todo.domain.TaskCategory

class HomeCategoryAdapter(
    private val onItemClick: (CategoryItem) -> Unit,
) : ListAdapter<CategoryItem, HomeCategoryAdapter.CategoryViewHolder>(CategoryDiffCallback) {

    private companion object CategoryDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
            oldItem.categoryType == newItem.categoryType

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
            oldItem == newItem
    }

    private var selectedCategory: TaskCategory? = null

    fun updateData(newItems: List<CategoryItem>, selectedCat: TaskCategory?) {
        this.selectedCategory = selectedCat
        submitList(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            HomeItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), selectedCategory, onItemClick)
    }

    class CategoryViewHolder(private val binding: HomeItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CategoryItem,
            selectedCategory: TaskCategory?,
            onClick: (CategoryItem) -> Unit
        ) {
            with(binding) {
                tvCatTitle.text = item.title
                ivCatIcon.setImageResource(item.iconRes)

                val color = ContextCompat.getColor(root.context, item.colorRes)
                tvCatTitle.setTextColor(color)
                ivCatIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)

                root.alpha = when {
                    selectedCategory == null -> 1.0f
                    selectedCategory == item.categoryType -> 1.0f
                    else -> 0.25f
                }

                root.setOnClickListener { onClick(item) }
            }
        }
    }
}