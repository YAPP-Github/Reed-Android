package com.ninecraft.booket.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordRegisterRequest(
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("quote")
    val quote: String,
    @SerialName("emotionTags")
    val emotionTags: List<String>,
    @SerialName("review")
    val review: String,
)
