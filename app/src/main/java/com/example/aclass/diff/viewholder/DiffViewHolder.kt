package com.example.aclass.diff.viewholder

import androidx.core.view.isVisible
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
        binding.firstCommitContainer.isVisible = true
        binding.divider.isVisible = true
        binding.secondCommitContainer.isVisible = true

        binding.firstCommitScrollView.scrollTo(0, 0)
        binding.firstCommit.text = diffItem.firstCommitLines
        binding.secondCommitScrollView.scrollTo(0, 0)
        binding.secondCommit.text = diffItem.secondCommitLines
        binding.firstCommitLineNumbers.text = diffItem.firstCommitLineNumbers
        binding.secondCommitLineNumbers.text = diffItem.secondCommitLineNumbers

        // if first or second commit lines is only new lines, then hide commit container
        // only necessary to show the new or deleted file, not sharing a side
        var isHideLeft = true

        for (char in diffItem.firstCommitLines) {
            if (char != '\n') {
                isHideLeft = false
                break;
            }
        }

        if (isHideLeft) {
            binding.firstCommitContainer.isVisible = false
            binding.divider.isVisible = false
        }

        var isHideRight = true

        for (char in diffItem.secondCommitLines) {
            if (char != '\n') {
                isHideRight = false
                break;
            }
        }

        if (isHideRight) {
            binding.secondCommitContainer.isVisible = false
            binding.divider.isVisible = false
        }
    }
}