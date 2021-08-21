package com.example.aclass.diff.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.databinding.ItemDiffSectionTwoFilesBinding
import com.example.aclass.diff.DiffRecyclerItem

class DiffSectionTwoFilesViewHolder(
    private val binding: ItemDiffSectionTwoFilesBinding
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

    fun bind(diffItem: DiffRecyclerItem.SectionTwoFiles) {
        binding.firstCommitScrollView.scrollTo(0, 0)
        binding.firstCommit.text = diffItem.firstCommitLines
        binding.secondCommitScrollView.scrollTo(0, 0)
        binding.secondCommit.text = diffItem.secondCommitLines
        binding.lineNumberRange.text = diffItem.lineNumberRange

        if (diffItem.lineNumberRange.contains("@@ -159,7")) {
            println("hey")
        }
    }
}