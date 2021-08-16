package com.example.aclass.common.repository

import com.example.aclass.common.network.GitHubService
import javax.inject.Inject

class RepoRepository @Inject constructor(private val gitHubService: GitHubService) {

    suspend fun getPullRequests(owner: String, repo: String) = gitHubService.getPullRequests(owner, repo)
}