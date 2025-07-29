package com.ninecraft.booket.feature.search.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ninecraft.booket.feature.screens.LibrarySearchScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.SearchScreen
import com.slack.circuit.codegen.annotations.CircuitInject
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

        fun handleEvent(event: LibrarySearchUiEvent) {
            when (event) {
                is LibrarySearchUiEvent.OnBackClick -> {
                    navigator.pop()
                }
            }
        }

        return LibrarySearchUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LibrarySearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LibrarySearchPresenter
    }
}
