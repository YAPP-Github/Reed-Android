package com.ninecraft.booket.core.common.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.UUID

object EventHelper {
    private val _eventFlow = Channel<ReedEvent>(Channel.BUFFERED)
    val eventFlow = _eventFlow.receiveAsFlow()

    fun sendEvent(event: ReedEvent) {
        _eventFlow.trySend(event)
    }
}

sealed interface ReedEvent {
    data class ShowDialog(
        val spec: DialogSpec,
        val key: String = UUID.randomUUID().toString(),
    ) : ReedEvent
}
