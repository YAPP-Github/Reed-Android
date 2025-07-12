package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class BookSearchModel(
    val version: String = "",
    val title: String = "",
    val link: String = "",
    val pubDate: String = "",
    val totalResults: Int = 0,
    val startIndex: Int = 0,
    val itemsPerPage: Int = 0,
    val query: String = "",
    val searchCategoryId: Int = 0,
    val searchCategoryName: String = "",
    val books: List<BookSummaryModel> = emptyList(),
)

@Stable
data class BookSummaryModel(
    val isbn: String = "",
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val coverImageUrl: String = "",
)
