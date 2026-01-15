package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadingRecordsResponse(
    @SerialName("lastPage")
    val lastPage: Boolean,
    @SerialName("totalResults")
    val totalResults: Int,
    @SerialName("startIndex")
    val startIndex: Int,
    @SerialName("itemsPerPage")
    val itemsPerPage: Int,
    @SerialName("readingRecords")
    val readingRecords: List<ReadingRecord>,
)

@Serializable
data class ReadingRecord(
    @SerialName("id")
    val id: String,
    @SerialName("userBookId")
    val userBookId: String,
    @SerialName("pageNumber")
    val pageNumber: Int?,
    @SerialName("quote")
    val quote: String,
    @SerialName("review")
    val review: String?,
    @SerialName("primaryEmotion")
    val primaryEmotion: PrimaryEmotion,
    @SerialName("detailEmotions")
    val detailEmotions: List<DetailEmotion>,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("bookTitle")
    val bookTitle: String?,
    @SerialName("bookPublisher")
    val bookPublisher: String?,
    @SerialName("bookCoverImageUrl")
    val bookCoverImageUrl: String?,
    @SerialName("author")
    val author: String?,
)

@Serializable
data class PrimaryEmotion(
    @SerialName("code")
    val code: String,
    @SerialName("displayName")
    val displayName: String,
)
