package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResponse(
    @SerialName("version")
    val version: String,
    @SerialName("title")
    val title: String,
    @SerialName("link")
    val link: String,
    @SerialName("pubDate")
    val pubDate: String,
    @SerialName("totalResults")
    val totalResults: Int,
    @SerialName("startIndex")
    val startIndex: Int,
    @SerialName("itemsPerPage")
    val itemsPerPage: Int,
    @SerialName("query")
    val query: String,
    @SerialName("searchCategoryId")
    val searchCategoryId: Int,
    @SerialName("searchCategoryName")
    val searchCategoryName: String,
    @SerialName("books")
    val books: List<BookSummary>,
)

@Serializable
data class BookSummary(
    @SerialName("isbn")
    val isbn: String,
    @SerialName("title")
    val title: String,
    @SerialName("author")
    val author: String,
    @SerialName("publisher")
    val publisher: String,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("userBookStatus")
    val userBookStatus: String,
)
