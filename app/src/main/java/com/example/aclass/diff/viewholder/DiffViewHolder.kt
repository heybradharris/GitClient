package com.example.aclass.diff.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.databinding.ItemDiffBinding
import com.example.aclass.diff.DiffRecyclerItem

class DiffViewHolder(
    private val binding: ItemDiffBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.firstCommitScrollView.setOnTouchListener { _, event ->
            binding.secondCommitScrollView.onTouchEvent(event)
            false
        }

        binding.secondCommitScrollView.setOnTouchListener { _, event ->
            binding.firstCommitScrollView.onTouchEvent(event)
            false
        }
    }

    fun bind(diffItem: DiffRecyclerItem.Diff) {
        binding.firstCommitScrollView.scrollTo(0, 0)
        binding.firstCommit.text = diffItem.firstCommitLines
        binding.secondCommitScrollView.scrollTo(0, 0)
        binding.secondCommit.text = diffItem.secondCommitLines
        binding.firstCommitLineNumbers.text = diffItem.firstCommitLineNumbers
        binding.secondCommitLineNumbers.text = diffItem.secondCommitLineNumbers

        // Hide side if blank
    }
}