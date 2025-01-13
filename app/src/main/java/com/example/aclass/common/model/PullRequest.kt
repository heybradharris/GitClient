package com.example.aclass.common.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PullRequest(
    @Json(name = "id") val id: Long,
    @Json(name = "diff_url") val diffUrl: String,
    @Json(name = "number") val number: Int,
    @Json(name = "state") val state: String,
    @Json(name = "title") val title: String,
    @Json(name = "body") val body: String?
)