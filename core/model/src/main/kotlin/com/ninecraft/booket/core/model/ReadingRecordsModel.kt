package com.ninecraft.booket.core.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

data class ReadingRecordsModel(
    val lastPage: Boolean = true,
    val totalResults: Int = 0,
    val startIndex: Int = 0,
    val itemsPerPage: Int = 0,
    val readingRecords: List<ReadingRecordModel> = emptyList(),
)

@Immutable
data class ReadingRecordModel(
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

@Stable
data class ReadingRecordModelV2(
    val id: String = "",
    val userBookId: String = "",
    val pageNumber: Int? = null,
    val quote: String = "",
    val review: String = "",
    val primaryEmotion: PrimaryEmotionModel = PrimaryEmotionModel(),
    val detailEmotions: List<DetailEmotionModel> = emptyList(),
    val createdAt: String = "",
    val updatedAt: String = "",
    val bookTitle: String = "",
    val bookPublisher: String = "",
    val bookCoverImageUrl: String = "",
    val author: String = "",
)

@Stable
data class PrimaryEmotionModel(
    val code: EmotionCode = EmotionCode.OTHER,
    val displayName: String = "기타",
)
