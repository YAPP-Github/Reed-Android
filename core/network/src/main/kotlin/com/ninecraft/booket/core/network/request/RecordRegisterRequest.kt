package com.ninecraft.booket.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordRegisterRequest(
    @SerialName("pageNumber")
    val pageNumber: Int?,
    @SerialName("quote")
    val quote: String,
    @SerialName("review")
    val review: String,
    @SerialName("primaryEmotion")
    val primaryEmotion: String,
    @SerialName("detailEmotionTagIds")
    val detailEmotionTagIds: List<String>,
)
