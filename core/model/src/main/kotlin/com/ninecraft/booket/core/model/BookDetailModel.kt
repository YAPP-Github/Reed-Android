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
    val isbn: String = "",
    val isbn13: String = "",
    val itemId: Long = 0L,
    val priceSales: Double = 0.0,
    val priceStandard: Double = 0.0,
    val mallType: String = "",
    val stockStatus: String = "",
    val mileage: Int = 0,
    val cover: String = "",
    val categoryId: Int = 0,
    val categoryName: String = "",
    val publisher: String = "",
)
