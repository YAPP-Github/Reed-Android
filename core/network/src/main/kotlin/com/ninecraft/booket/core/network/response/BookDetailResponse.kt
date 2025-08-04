package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailResponse(
    @SerialName("version")
    val version: String,
    @SerialName("title")
    val title: String,
    @SerialName("link")
    val link: String,
    @SerialName("author")
    val author: String,
    @SerialName("pubDate")
    val pubDate: String,
    @SerialName("description")
    val description: String,
    @SerialName("isbn13")
    val isbn13: String,
    @SerialName("mallType")
    val mallType: String,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("categoryName")
    val categoryName: String,
    @SerialName("publisher")
    val publisher: String,
    @SerialName("totalPage")
    val totalPage: Int,
    @SerialName("userBookStatus")
    val userBookStatus: String,
)
