package com.easyhooon.pokedex.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HealthCheckResponse(
    @SerialName("status")
    val status: String,
)
