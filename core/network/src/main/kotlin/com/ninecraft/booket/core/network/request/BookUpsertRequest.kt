package com.ninecraft.booket.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookUpsertRequest(
    @SerialName("bookIsbn")
    val bookIsbn: String,
    @SerialName("bookStatus")
    val bookStatus: String,
)
