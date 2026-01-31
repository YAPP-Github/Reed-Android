package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmotionGroupsResponse(
    @SerialName("emotions")
    val emotions: List<EmotionGroup>,
)

@Serializable
data class EmotionGroup(
    @SerialName("code")
    val code: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("detailEmotions")
    val detailEmotions: List<DetailEmotion>,
)

@Serializable
data class DetailEmotion(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
)
