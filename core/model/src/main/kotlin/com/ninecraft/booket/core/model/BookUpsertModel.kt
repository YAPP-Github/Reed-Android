package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class BookUpsertModel(
    val userBookId: String,
    val userId: String,
    val isbn13: String,
    val bookTitle: String,
    val bookAuthor: String,
    val status: String,
    val coverImageUrl: String,
    val publisher: String,
    val createdAt: String,
    val updatedAt: String,
    val recordCount: Int,
)
