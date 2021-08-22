package com.example.aclass

import com.example.aclass.common.model.PullRequest
import com.example.aclass.pullrequests.PullRequestFilter
import junit.framework.Assert.fail
import org.junit.Test

val pullRequestItems = listOf(
    PullRequest(1, "diff_url", 1, "closed", "title", "body"),
    PullRequest(2, "diff_url", 2, "open", "title", "body"),
    PullRequest(3, "diff_url", 3, "closed", "title", "body"),
    PullRequest(4, "diff_url", 4, "open", "title", "body"),
    PullRequest(5, "diff_url", 5, "closed", "title", "body"),
)

val openPullRequestItemIds = listOf(
    2,
    4
)

class PullRequestFilterUnitTest {

    @Test
    fun `when filter open pull requests, check filterOnlyOpen() returns the correct filtered list`() {
        val openPullRequestItems = PullRequestFilter.filterOnlyOpen(pullRequestItems)

        if (openPullRequestItems.size != openPullRequestItemIds.size) {
            fail("$TAG: Test setup failed, can't have different size lists")
        }

        for ((index, pullRequest) in openPullRequestItems.withIndex()) {
            assert(pullRequest.id == openPullRequestItemIds[index]) { "$TAG: Expected openPullRequestItems id to match openPullRequestItemIds" }
        }
    }

    companion object {
        val TAG: String = this::class.java.simpleName
    }
}