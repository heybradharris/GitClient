package com.example.aclass.pullrequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.common.model.PullRequest
import com.example.aclass.databinding.ItemPullRequestBinding

class PullRequestAdapter : RecyclerView.Adapter<PullRequestViewHolder>() {

    private val pullRequestItems = ArrayList<PullRequest>()

    override fun getItemCount() = pullRequestItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestViewHolder {
        val binding = ItemPullRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PullRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PullRequestViewHolder, position: Int) {
        holder.onBind(pullRequestItems[position])
    }

    fun submitList(newPullRequestItems: List<PullRequest>) {
        val diffCallback = PullRequestDiffCallback(pullRequestItems, newPullRequestItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        pullRequestItems.clear()
        pullRequestItems.addAll(newPullRequestItems)
        diffResult.dispatchUpdatesTo(this)
    }
}