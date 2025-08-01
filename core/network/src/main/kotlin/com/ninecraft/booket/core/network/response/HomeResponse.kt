package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    @SerialName("recentBooks")
    val recentBooks: List<RecentBook>,
)

@Serializable
data class RecentBook(
    @SerialName("userBookId")
    val userBookId: String,
    @SerialName("title")
    val title: String,
    @SerialName("author")
    val author: String,
    @SerialName("publisher")
    val publisher: String,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("lastRecordedAt")
    val lastRecordedAt: String,
    @SerialName("recordCount")
    val recordCount: Int,
)
