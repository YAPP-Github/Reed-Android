package com.ninecraft.booket.feature.detail.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.RecordCardScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class RecordCardPresenter @AssistedInject constructor(
    @Assisted private val screen: RecordCardScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<RecordCardUiState> {
    @Composable
    override fun present(): RecordCardUiState {
        var isLoading by rememberRetained { mutableStateOf(false) }
        var isCapturing by rememberRetained { mutableStateOf(false) }
        var isSharing by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<RecordCardSideEffect?>(null) }

        fun captureRecordCard() {

        }

        fun shareRecordCard() {

        }

        fun handleEvent(event: RecordCardUiEvent) {
            when (event) {
                is RecordCardUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is RecordCardUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is RecordCardUiEvent.OnSaveClick -> {
                    isCapturing = true
                }
                is RecordCardUiEvent.OnShareClick -> {
                    isSharing = true
                }
                is RecordCardUiEvent.CaptureRecordCard -> TODO()
                is RecordCardUiEvent.SaveRecordCard -> TODO()
                is RecordCardUiEvent.ShareRecordCard -> TODO()
            }
        }

        return RecordCardUiState(
            isLoading = isLoading,
            quote = screen.quote,
            bookTitle = screen.bookTitle,
            author = screen.author,
            emotionTag = screen.emotionTag,
            isCapturing = isCapturing,
            isSharing = isSharing,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

@CircuitInject(RecordCardScreen::class, ActivityRetainedComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(
        screen: RecordCardScreen,
        navigator: Navigator,
    ): RecordCardPresenter
}
