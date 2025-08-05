package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordDetailResponse(
    @SerialName("id")
    val id: String,
    @SerialName("userBookId")
    val userBookId: String,
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("quote")
    val quote: String,
    @SerialName("review")
    val review: String,
    @SerialName("emotionTags")
    val emotionTags: List<String>,
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
    @SerialName("author")
    val author: String,
)
