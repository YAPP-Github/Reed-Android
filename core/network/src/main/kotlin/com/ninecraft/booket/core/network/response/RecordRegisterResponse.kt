package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordRegisterResponse(
    @SerialName("id")
    val id: String,
    @SerialName("userBookId")
    val userBookId: String,
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("quote")
    val quote: String,
    @SerialName("emotionTags")
    val emotionTags: List<String>,
    @SerialName("review")
    val review: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
)
