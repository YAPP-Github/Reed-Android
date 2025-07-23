package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class LibraryModel(
    val books: LibraryBooksModel = LibraryBooksModel(),
    val totalCount: Int = 0,
    val beforeReadingCount: Int = 0,
    val readingCount: Int = 0,
    val completedCount: Int = 0,
)

@Stable
data class LibraryBooksModel(
    val content: List<LibraryBookSummaryModel> = emptyList(),
    val page: PageInfoModel = PageInfoModel(),
)

@Stable
data class LibraryBookSummaryModel(
    val userBookId: String = "",
    val userId: String = "",
    val bookIsbn: String = "",
    val bookTitle: String = "",
    val bookAuthor: String = "",
    val status: String = "",
    val recordCount: Int = 0,
    val coverImageUrl: String = "",
    val publisher: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
)

@Stable
data class PageInfoModel(
    val size: Int = 0,
    val number: Int = 0,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
)
