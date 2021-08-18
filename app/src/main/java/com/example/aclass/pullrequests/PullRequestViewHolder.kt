package com.example.aclass.pullrequests

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.aclass.common.model.PullRequest
import com.example.aclass.databinding.ItemPullRequestBinding
import java.lang.IllegalArgumentException

class PullRequestViewHolder(
    private val binding : ItemPullRequestBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    var pullRequest: PullRequest? = null

    init {
        binding.root.setOnClickListener(this)
    }

    fun onBind(pullRequest: PullRequest) {
        this.pullRequest = pullRequest

        with(binding) {
            title.text = pullRequest.title
            state.text = pullRequest.state
            body.text = pullRequest.body
            number.text = "#${pullRequest.number}"
        }
        val textColor = if (pullRequest.state == "open")
            Color.GREEN
        else
            Color.RED

        binding.state.setTextColor(textColor)
    }

    override fun onClick(v: View) {
        val diffUrl = pullRequest?.diffUrl ?: throw IllegalArgumentException("$TAG: diffUrl is not set")

        val number = pullRequest?.number.toString() ?: throw IllegalArgumentException("$TAG: number is not set")

        val path = diffUrl.substringAfter("com/").split("/")

        val action = PullRequestFragmentDirections.actionPullRequestFragmentToDiffFragment(
            path[0],
            path[1],
            number,
        )

        Log.d(TAG,"opening diff owner:${path[0]} repo:${path[1]} number:${number}")

        v.findNavController().navigate(action)
    }

    companion object {
        val TAG: String = this::class.java.simpleName
    }
}