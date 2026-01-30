package com.ninecraft.booket.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class BookSearchModel(
    val version: String = "",
    val title: String = "",
    val pubDate: String = "",
    val totalResults: Int = 0,
    val startIndex: Int = 0,
    val itemsPerPage: Int = 0,
    val query: String = "",
    val searchCategoryId: Int = 0,
    val searchCategoryName: String = "",
    val lastPage: Boolean = false,
    val books: List<BookSummaryModel> = emptyList(),
)

@Immutable
data class BookSummaryModel(
    val isbn13: String = "",
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val coverImageUrl: String = "",
    val link: String = "",
    val userBookStatus: String = "",
    val key: String = "",
) {
    val isRegistered: Boolean
        get() = userBookStatus != BEFORE_REGISTRATION

    companion object {
        const val BEFORE_REGISTRATION = "BEFORE_REGISTRATION"
    }
}
