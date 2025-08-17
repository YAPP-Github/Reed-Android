package com.ninecraft.booket.feature.edit.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.EmotionEditScreen
import com.ninecraft.booket.feature.screens.RecordEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class RecordEditPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RecordEditUiState> {
    @Composable
    override fun present(): RecordEditUiState {
        var sideEffect by rememberRetained { mutableStateOf<RecordEditSideEffect?>(null) }

        fun handleEvent(event: RecordEditUiEvent) {
            when (event) {
                RecordEditUiEvent.OnCloseClick -> {
                    navigator.pop()
                }

                RecordEditUiEvent.OnEmotionEditClick -> {
                    navigator.goTo(EmotionEditScreen)
                }
            }
        }
        return RecordEditUiState(
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

@CircuitInject(RecordEditScreen::class, ActivityRetainedComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(
        navigator: Navigator,
    ): RecordEditPresenter
}
