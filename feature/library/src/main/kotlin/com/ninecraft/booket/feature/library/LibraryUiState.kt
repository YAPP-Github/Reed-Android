package com.ninecraft.booket.feature.library

import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data class Error(val message: String) : UiState
}

data class LibraryUiState(
    val uiState: UiState = UiState.Idle,
    val footerState: FooterState = FooterState.Idle,
    val filterChips: ImmutableList<LibraryFilterChip> =
        LibraryFilterOption.entries.map { LibraryFilterChip(option = it, count = 0) }.toPersistentList(),
    val currentFilter: LibraryFilterOption = LibraryFilterOption.TOTAL,
    val books: ImmutableList<LibraryBookSummaryModel> = persistentListOf(),
    val sideEffect: LibrarySideEffect? = null,
    val eventSink: (LibraryUiEvent) -> Unit,
) : CircuitUiState

sealed interface LibrarySideEffect {
    data class ShowToast(val message: String) : LibrarySideEffect
}

sealed interface LibraryUiEvent : CircuitUiEvent {
    data object InitSideEffect : LibraryUiEvent
    data object OnSettingsClick : LibraryUiEvent
    data class OnBookClick(val isbn: String) : LibraryUiEvent
    data object OnLoadMore : LibraryUiEvent
    data object OnRetryClick : LibraryUiEvent
    data class OnFilterClick(val filterOption: LibraryFilterOption) : LibraryUiEvent
}

data class LibraryFilterChip(
    val option: LibraryFilterOption,
    val count: Int,
)

enum class LibraryFilterOption(val value: String) {
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

    fun getApiValue(): String? {
        return if (this == TOTAL) {
            null
        } else {
            this.value
        }
    }

    companion object Companion {
        fun fromValue(value: String): LibraryFilterOption? {
            return entries.find { it.value == value }
        }
    }
}
