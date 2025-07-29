package com.ninecraft.booket.feature.search.library

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.LibrarySearchScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class LibrarySearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<LibrarySearchUiState> {

    @Composable
    override fun present(): LibrarySearchUiState {
        val scope = rememberCoroutineScope()

        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        val queryState = rememberTextFieldState()

        fun handleEvent(event: LibrarySearchUiEvent) {
            when (event) {
                is LibrarySearchUiEvent.OnBackClick -> {
                    navigator.pop()
                }
                is LibrarySearchUiEvent.OnSearchClick -> {}
                is LibrarySearchUiEvent.OnClearClick -> {
                    queryState.clearText()
                }
            }
        }

        return LibrarySearchUiState(
            uiState = uiState,
            queryState = queryState,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LibrarySearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LibrarySearchPresenter
    }
}
