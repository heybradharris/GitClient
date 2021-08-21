package com.example.aclass.diff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.R
import com.example.aclass.databinding.ItemDiffSectionOneFileBinding
import com.example.aclass.databinding.ItemDiffSectionTwoFilesBinding
import com.example.aclass.databinding.ItemDiffTitleBinding
import com.example.aclass.diff.viewholder.DiffSectionOneFileViewHolder
import com.example.aclass.diff.viewholder.DiffSectionTwoFilesViewHolder
import com.example.aclass.diff.viewholder.DiffTitleViewHolder

class DiffRecyclerAdapter(
    private val diffItems: List<DiffRecyclerItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = diffItems.size

    override fun getItemViewType(position: Int): Int {
        return when (diffItems[position]) {
            is DiffRecyclerItem.Title -> R.layout.item_diff_title
            is DiffRecyclerItem.SectionTwoFiles -> R.layout.item_diff_section_two_files
            is DiffRecyclerItem.SectionOneFile -> R.layout.item_diff_section_one_file
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_diff_title -> {
                val binding = ItemDiffTitleBinding.inflate(layoutInflater, parent, false)
                DiffTitleViewHolder(binding)
            }
            R.layout.item_diff_section_two_files -> {
                val binding = ItemDiffSectionTwoFilesBinding.inflate(layoutInflater, parent, false)
                DiffSectionTwoFilesViewHolder(binding)
            }
            R.layout.item_diff_section_one_file -> {
                val binding = ItemDiffSectionOneFileBinding.inflate(layoutInflater, parent, false)
                DiffSectionOneFileViewHolder(binding)
            }
            else -> throw Exception("Unknown viewType for DiffAdapter $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = diffItems[position]) {
            is DiffRecyclerItem.Title -> { (holder as DiffTitleViewHolder).bind(item) }
            is DiffRecyclerItem.SectionTwoFiles -> { (holder as DiffSectionTwoFilesViewHolder).bind(item) }
            is DiffRecyclerItem.SectionOneFile -> { (holder as DiffSectionOneFileViewHolder). bind(item) }
        }
    }


}