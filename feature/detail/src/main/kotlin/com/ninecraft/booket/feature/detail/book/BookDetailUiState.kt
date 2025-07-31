package com.ninecraft.booket.feature.detail.book

import com.ninecraft.booket.core.common.R
import com.ninecraft.booket.core.common.constants.BookStatus
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class BookDetailUiState(
    val sideEffect: BookDetailSideEffect? = null,
    val eventSink: (BookDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface BookDetailSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : BookDetailSideEffect
}

sealed interface BookDetailUiEvent : CircuitUiEvent {
    data object InitSideEffect : BookDetailUiEvent
    data object OnBackClicked : BookDetailUiEvent
    data object OnBeforeReadingClick : BookDetailUiEvent
    data object OnReadingClick : BookDetailUiEvent
    data object OnCompletedClick : BookDetailUiEvent
    data class OnRecordItemClick(val recordId: String) : BookDetailUiEvent
}

enum class RecordSort(val value: String) {
    PAGE_ASCENDING("BEFORE_READING"),
    RECENT_REGISTER("READING"),
    ;

    fun getDisplayNameRes(): Int {
        return when (this) {
            PAGE_ASCENDING -> R.string.record_sort_page_ascending
            RECENT_REGISTER -> R.string.record_sort_recent_register
        }
    }

    companion object Companion {
        fun fromValue(value: String): RecordSort? {
            return entries.find { it.value == value }
        }
    }
}
