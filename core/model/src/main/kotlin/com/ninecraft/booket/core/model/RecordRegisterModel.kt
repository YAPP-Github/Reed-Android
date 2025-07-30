package com.ninecraft.booket.core.model

data class RecordRegisterModel(
    val id: String = "",
    val userBookId: String = "",
    val pageNumber: Int = 0,
    val quote: String = "",
    val emotionTags: List<String> = emptyList(),
    val review: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
)
