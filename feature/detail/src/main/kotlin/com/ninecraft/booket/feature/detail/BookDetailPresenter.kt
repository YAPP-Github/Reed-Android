package com.ninecraft.booket.feature.detail

import androidx.compose.runtime.Composable
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class BookDetailPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
) : Presenter<BookDetailUiState> {

    @Composable
    override fun present(): BookDetailUiState {
        fun handleEvent(event: BookDetailUiEvent) {
            when (event) {
                BookDetailUiEvent.OnBackClicked -> {
                    navigator.pop()
                }
            }
        }

        return BookDetailUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(BookDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): BookDetailPresenter
    }
}


