package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class BookSearchModel(
    val version: String,
    val title: String,
    val link: String,
    val pubDate: String,
    val totalResults: Int,
    val startIndex: Int,
    val itemsPerPage: Int,
    val query: String,
    val searchCategoryId: Int,
    val searchCategoryName: String,
    val books: List<BookSummaryModel>,
)

@Stable
data class BookSummaryModel(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val coverImageUrl: String,
)
