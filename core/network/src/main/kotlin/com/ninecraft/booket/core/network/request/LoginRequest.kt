package com.ninecraft.booket.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("providerType")
    val providerType: String,
    @SerialName("oauthToken")
    val oauthToken: String,
)
