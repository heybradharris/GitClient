package com.example.aclass.diff.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.databinding.ItemDiffTitleBinding
import com.example.aclass.diff.DiffRecyclerItem

class DiffTitleViewHolder(
    private val binding: ItemDiffTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(diffItem: DiffRecyclerItem.Title) {
        binding.title.text = diffItem.title
    }
}