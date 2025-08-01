package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class HomeModel(
    val recentBooks: List<RecentBookModel> = emptyList()
)

@Stable
data class RecentBookModel(
    val userBookId: String = "",
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val coverImageUrl: String = "",
    val lastRecordedAt: String = "",
    val recordCount: Int = 0,
)
