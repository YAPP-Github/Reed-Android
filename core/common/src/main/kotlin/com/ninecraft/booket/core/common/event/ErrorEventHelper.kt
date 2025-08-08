package com.ninecraft.booket.core.common.event

import com.ninecraft.booket.core.common.constants.ErrorDialogSpec
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.UUID

object ErrorEventHelper {
    private val _errorEvent = Channel<ErrorEvent>(DEFAULT_BUFFER_SIZE)
    val errorEvent = _errorEvent.receiveAsFlow()

    fun sendError(event: ErrorEvent) {
        _errorEvent.trySend(event)
    }
}

sealed interface ErrorEvent {
    data class ShowDialog(
        val spec: ErrorDialogSpec,
        val key: String = UUID.randomUUID().toString(),
    ) : ErrorEvent
}
