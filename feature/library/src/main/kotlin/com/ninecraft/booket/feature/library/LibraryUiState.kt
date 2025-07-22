package com.ninecraft.booket.feature.library

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

data class LibraryUiState(
    val isLoading: Boolean = false,
    val filterElements: ImmutableList<FilterChipState>,
    val sideEffect: LibrarySideEffect? = null,
    val eventSink: (LibraryUiEvent) -> Unit,
) : CircuitUiState

sealed interface LibrarySideEffect {
    data class ShowToast(val message: String) : LibrarySideEffect
}

sealed interface LibraryUiEvent : CircuitUiEvent {
    data object InitSideEffect : LibraryUiEvent
    data object OnSettingsClick : LibraryUiEvent
    data class OnFilterClick(val bookStatus: BookStatus) : LibraryUiEvent
}

data class FilterChipState(
    val title: BookStatus,
    val count: Int,
    val isSelected: Boolean = false,
)

enum class BookStatus(val value: String) {
    TOTAL("TOTAL"),
    BEFORE_READING("BEFORE_READING"),
    READING("READING"),
    COMPLETED("COMPLETED"),
    ;

    fun getDisplayNameRes(): Int {
        return when (this) {
            TOTAL -> R.string.library_filter_total
            BEFORE_READING -> R.string.library_filter_before_reading
            READING -> R.string.library_filter_reading
            COMPLETED -> R.string.library_filter_completed
        }
    }

    companion object Companion {
        fun fromValue(value: String): BookStatus? {
            return entries.find { it.value == value }
        }
    }
}
