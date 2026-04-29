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
) : RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition: Int = -1

    class CategoryViewHolder(val binding: HomeItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CategoryItem,
            position: Int,
            selectedPosition: Int,
            onClick: (Int) -> Unit
        ) {
            with(binding) {
                tvCatTitle.text = item.title
                ivCatIcon.setImageResource(item.iconRes)

                val color = ContextCompat.getColor(root.context, item.colorRes)
                tvCatTitle.setTextColor(color)
                ivCatIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)

                root.alpha = when {
                    selectedPosition == -1 -> 1.0f
                    selectedPosition == position -> 1.0f
                    else -> 0.25f
                }

                root.setOnClickListener {
                    onClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = HomeItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position], position, selectedPosition) { clickedPos ->

            if (selectedPosition == clickedPos) {
                selectedPosition = -1
            } else {
                selectedPosition = clickedPos
            }
            notifyDataSetChanged()
            onItemClick(items[position], clickedPos)
        }
    }

    override fun getItemCount() = items.size
}