package com.example.aclass.pullrequests

import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.common.model.PullRequest
import com.example.aclass.databinding.ItemPullRequestBinding

class PullRequestViewHolder(
    private val binding : ItemPullRequestBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(pullRequest: PullRequest) {

    }
}