package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeedResponse(
    @SerialName("categories")
    val categories: List<Category>,
)

@Serializable
data class Category(
    @SerialName("name")
    val name: String,
    @SerialName("count")
    val count: Int,
)
