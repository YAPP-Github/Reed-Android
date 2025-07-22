package com.ninecraft.booket.feature.record.register

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

class RecordRegisterPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RecordUiState> {

    @Composable
    override fun present(): RecordUiState {
        var isLoading by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: RecordRegisterUiEvent) {
            when (event) {
                is RecordRegisterUiEvent.OnBackButtonClick -> navigator.pop()
                is RecordRegisterUiEvent.OnNextButtonClick -> {}
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
        fun create(navigator: Navigator): RecordRegisterPresenter
    }
}
