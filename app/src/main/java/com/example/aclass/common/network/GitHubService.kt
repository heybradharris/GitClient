package com.example.aclass.common.network

import com.example.aclass.common.model.PullRequest
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GitHubService {

    @Headers("accept: application/vnd.github.v3+json")
    @GET("/repos/{owner}/{repo}/pulls?state=all")
    suspend fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): List<PullRequest>

    @GET("https://github.com/{owner}/{repo}/pull/{number}.diff")
    suspend fun getDiff(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Int
    ): String
}