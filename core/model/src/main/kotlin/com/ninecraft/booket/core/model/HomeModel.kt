package com.ninecraft.booket.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class HomeModel(
    val recentBooks: List<RecentBookModel> = emptyList(),
)

@Immutable
data class RecentBookModel(
    val userBookId: String = "",
    val isbn13: String = "",
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val coverImageUrl: String = "",
    val lastRecordedAt: String = "",
    val recordCount: Int = 0,
)
