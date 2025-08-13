package com.ninecraft.booket.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookUpsertRequest(
    @SerialName("isbn13")
    val isbn13: String,
    @SerialName("bookStatus")
    val bookStatus: String,
)
