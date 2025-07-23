package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryResponse(
    @SerialName("books")
    val books: LibraryBooks,
    @SerialName("totalCount")
    val totalCount: Int = 0,
    @SerialName("beforeReadingCount")
    val beforeReadingCount: Int,
    @SerialName("readingCount")
    val readingCount: Int,
    @SerialName("completedCount")
    val completedCount: Int
)

@Serializable
data class LibraryBooks(
    @SerialName("content")
    val content: List<LibraryBookSummary>,
    @SerialName("page")
    val page: PageInfo
)

@Serializable
data class LibraryBookSummary(
    @SerialName("userBookId")
    val userBookId: String,
    @SerialName("userId")
    val userId: String,
    @SerialName("bookIsbn")
    val bookIsbn: String,
    @SerialName("bookTitle")
    val bookTitle: String,
    @SerialName("bookAuthor")
    val bookAuthor: String,
    @SerialName("status")
    val status: String,
    @SerialName("recordCount")
    val recordCount: Int = 0,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("publisher")
    val publisher: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)

@Serializable
data class PageInfo(
    @SerialName("size")
    val size: Int,
    @SerialName("number")
    val number: Int,
    @SerialName("totalElements")
    val totalElements: Int,
    @SerialName("totalPages")
    val totalPages: Int
)
