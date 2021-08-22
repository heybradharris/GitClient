package com.example.aclass.pullrequests

import com.example.aclass.common.model.PullRequest

class PullRequestFilter {
    companion object {
        fun filterOnlyOpen(pullRequestItems: List<PullRequest>) : List<PullRequest> {
            return pullRequestItems.filter { it.state == "open" }
        }
    }
}