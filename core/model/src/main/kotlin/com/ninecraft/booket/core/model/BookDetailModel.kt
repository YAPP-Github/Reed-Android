package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class BookDetailModel(
    val version: String,
    val title: String,
    val link: String,
    val author: String,
    val pubDate: String,
    val description: String,
    val isbn: String,
    val isbn13: String,
    val itemId: Long,
    val priceSales: Double,
    val priceStandard: Double,
    val mallType: String,
    val stockStatus: String,
    val mileage: Int,
    val cover: String,
    val categoryId: Int,
    val categoryName: String,
    val publisher: String,
)
