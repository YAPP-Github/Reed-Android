package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailResponse(
    @SerialName("version")
    val version: String,
    @SerialName("title")
    val title: String,
    @SerialName("link")
    val link: String,
    @SerialName("author")
    val author: String,
    @SerialName("pubDate")
    val pubDate: String,
    @SerialName("description")
    val description: String,
    @SerialName("isbn")
    val isbn: String,
    @SerialName("isbn13")
    val isbn13: String,
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("priceSales")
    val priceSales: Double,
    @SerialName("priceStandard")
    val priceStandard: Double,
    @SerialName("mallType")
    val mallType: String,
    @SerialName("stockStatus")
    val stockStatus: String,
    @SerialName("mileage")
    val mileage: Int,
    @SerialName("cover")
    val cover: String,
    @SerialName("categoryId")
    val categoryId: Int,
    @SerialName("categoryName")
    val categoryName: String,
    @SerialName("publisher")
    val publisher: String,
)
