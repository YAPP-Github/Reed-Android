package com.ninecraft.booket.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("status")
    val status: String,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
) {
    fun getErrorMessage(): String {
        return message.takeIf { it.isNotBlank() } ?: "알 수 없는 오류가 발생했습니다"
    }
}
