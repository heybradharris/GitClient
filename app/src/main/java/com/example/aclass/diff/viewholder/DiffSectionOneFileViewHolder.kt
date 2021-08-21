package com.example.aclass.diff.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.databinding.ItemDiffSectionOneFileBinding
import com.example.aclass.diff.DiffRecyclerItem

class DiffSectionOneFileViewHolder(
    private val binding: ItemDiffSectionOneFileBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(diffItem: DiffRecyclerItem.SectionOneFile) {
        binding.firstCommit.text = diffItem.commitLines
        binding.lineNumberRange.text = diffItem.lineNumberRange

        if (diffItem.lineNumberRange.contains("@@ -159,7")) {
            println("hey")
        }
    }
}