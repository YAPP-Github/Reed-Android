package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadingRecordsResponse(
    @SerialName("content")
    val content: List<ReadingRecord>,
    @SerialName("page")
    val page: PageInfo,
)

@Serializable
data class ReadingRecord(
    @SerialName("id")
    val id: String,
    @SerialName("userBookId")
    val userBookId: String,
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("quote")
    val quote: String,
    @SerialName("review")
    val review: String = "",
    @SerialName("emotionTags")
    val emotionTags: List<String> = emptyList(),
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("bookTitle")
    val bookTitle: String,
    @SerialName("bookPublisher")
    val bookPublisher: String,
    @SerialName("bookCoverImageUrl")
    val bookCoverImageUrl: String,
)
