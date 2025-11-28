package com.ninecraft.booket.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceRegistrationRequest(
    @SerialName("deviceId")
    val deviceId: String,
    @SerialName("fcmToken")
    val fcmToken: String,
)
