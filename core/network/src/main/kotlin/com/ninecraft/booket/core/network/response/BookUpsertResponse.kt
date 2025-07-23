package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookUpsertResponse(
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
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("publisher")
    val publisher: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
)
