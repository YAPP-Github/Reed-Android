package com.ninecraft.booket.core.model

data class ReadingRecordsModel(
    val lastPage: Boolean = true,
    val totalResults: Int = 0,
    val startIndex: Int = 0,
    val itemsPerPage: Int = 0,
    val readingRecords: List<ReadingRecordModel> = emptyList(),
)

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
