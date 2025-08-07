package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class BookDetailModel(
    val version: String = "",
    val title: String = "",
    val link: String = "",
    val author: String = "",
    val pubDate: String = "",
    val description: String = "",
    val isbn13: String = "",
    val mallType: String = "",
    val coverImageUrl: String = "",
    val categoryId: Int = 0,
    val categoryName: String = "",
    val publisher: String = "",
    val totalPage: Int = 0,
    val userBookStatus: String = "",
)
