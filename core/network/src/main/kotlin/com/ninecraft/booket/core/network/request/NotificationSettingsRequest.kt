package com.ninecraft.booket.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingsRequest(
    @SerialName("notificationEnabled")
    val notificationEnabled: Boolean,
)
