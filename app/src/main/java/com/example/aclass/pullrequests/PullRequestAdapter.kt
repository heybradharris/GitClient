package com.example.aclass.pullrequests

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.common.model.PullRequest

class PullRequestAdapter : RecyclerView.Adapter<PullRequestViewHolder>() {

    private val pullRequestItems = ArrayList<PullRequest>()

    override fun getItemCount() = pullRequestItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PullRequestViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}