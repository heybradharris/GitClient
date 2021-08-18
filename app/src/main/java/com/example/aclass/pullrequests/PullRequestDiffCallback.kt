package com.example.aclass.pullrequests

import androidx.recyclerview.widget.DiffUtil
import com.example.aclass.common.model.PullRequest

class PullRequestDiffCallback(
    private val oldList: List<PullRequest>,
    private val newList: List<PullRequest>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, _, number, state, title, body) = oldList[oldItemPosition]
        val (_, _, number1, state1, title1, body1) = newList[newItemPosition]

        return number == number1 &&
                state == state1 &&
                title == title1 &&
                body == body1
    }
}