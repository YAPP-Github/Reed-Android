package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class RecordDetailModel(
    val id: String = "",
    val userBookId: String = "",
    val pageNumber: Int = 0,
    val quote: String = "",
    val review: String = "",
    val emotionTags: List<String> = emptyList(),
    val createdAt: String = "",
    val updatedAt: String = "",
    val bookTitle: String = "",
    val bookPublisher: String = "",
    val bookCoverImageUrl: String = "",
    val author: String = "",
)
