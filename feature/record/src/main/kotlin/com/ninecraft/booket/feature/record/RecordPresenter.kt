package com.ninecraft.booket.feature.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.RecordScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class RecordPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RecordUiState> {

    @Composable
    override fun present(): RecordUiState {
        var isLoading by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: RecordUiEvent) {
            when (event) {
                is RecordUiEvent.OnBackButtonClick -> navigator.pop()
            }
        }

        return RecordUiState(
            isLoading = isLoading,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): RecordPresenter
    }
}
