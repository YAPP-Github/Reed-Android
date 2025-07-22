package com.ninecraft.booket.feature.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.LibraryScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

class LibraryPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<LibraryUiState> {

    @Composable
    override fun present(): LibraryUiState {
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<LibrarySideEffect?>(null) }
        var chipElements by rememberRetained {
            mutableStateOf(
                persistentListOf(
                    FilterChipState(
                        title = BookStatus.TOTAL,
                        count = 10,
                        isSelected = true,
                    ),
                    FilterChipState(
                        title = BookStatus.BEFORE_READING,
                        count = 15,
                        isSelected = false,
                    ),
                    FilterChipState(
                        title = BookStatus.READING,
                        count = 2,
                        isSelected = false,
                    ),
                    FilterChipState(
                        title = BookStatus.COMPLETED,
                        count = 5,
                        isSelected = false,
                    ),
                ),
            )
        }

        fun handleEvent(event: LibraryUiEvent) {
            when (event) {
                is LibraryUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is LibraryUiEvent.OnSettingsClick -> {
                    navigator.goTo(SettingsScreen)
                }

                is LibraryUiEvent.OnFilterClick -> {
                    chipElements = chipElements.map {
                        if (it.title == event.bookStatus) {
                            it.copy(isSelected = true)
                        } else {
                            it.copy(isSelected = false)
                        }
                    }.toPersistentList()
                    // TODO: 필터에 해당하는 도서 목록을 불러오는 로직이 들어가야 함
                }
            }
        }

        return LibraryUiState(
            isLoading = isLoading,
            filterElements = chipElements,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LibraryScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LibraryPresenter
    }
}
