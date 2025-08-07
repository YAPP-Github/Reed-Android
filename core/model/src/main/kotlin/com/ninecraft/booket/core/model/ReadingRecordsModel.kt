package com.ninecraft.booket.core.model

data class ReadingRecordsModel(
    val content: List<ReadingRecordModel> = emptyList(),
    val page: PageInfoModel = PageInfoModel(),
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
)
