package com.example.aclass.diff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.R
import com.example.aclass.databinding.ItemDiffBinding
import com.example.aclass.databinding.ItemDiffTitleBinding
import com.example.aclass.diff.viewholder.DiffViewHolder
import com.example.aclass.diff.viewholder.DiffTitleViewHolder

class DiffRecyclerAdapter(
    private val diffItems: List<DiffRecyclerItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = diffItems.size

    override fun getItemViewType(position: Int): Int {
        return when (diffItems[position]) {
            is DiffRecyclerItem.Title -> R.layout.item_diff_title
            is DiffRecyclerItem.Diff -> R.layout.item_diff
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_diff_title -> {
                val binding = ItemDiffTitleBinding.inflate(layoutInflater, parent, false)
                DiffTitleViewHolder(binding)
            }
            R.layout.item_diff -> {
                val binding = ItemDiffBinding.inflate(layoutInflater, parent, false)
                DiffViewHolder(binding)
            }
            else -> throw Exception("Unknown viewType for DiffAdapter $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = diffItems[position]) {
            is DiffRecyclerItem.Title -> { (holder as DiffTitleViewHolder).bind(item) }
            is DiffRecyclerItem.Diff -> { (holder as DiffViewHolder).bind(item) }
        }
    }


}