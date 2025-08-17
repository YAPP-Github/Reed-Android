package com.ninecraft.booket.feature.edit.record

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    @Assisted private val screen: RecordEditScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<RecordEditUiState> {
    @Composable
    override fun present(): RecordEditUiState {
        val recordInfo = screen.recordInfo
        val recordPageState = rememberTextFieldState(recordInfo.pageNumber.toString())
        val recordQuoteState = rememberTextFieldState(recordInfo.quote)
        val recordImpressionState = rememberTextFieldState(recordInfo.review)
        val isPageError by remember {
            derivedStateOf {
                val page = recordPageState.text.toString().toIntOrNull() ?: 0
                page > MAX_PAGE
            }
        }
        var sideEffect by rememberRetained { mutableStateOf<RecordEditSideEffect?>(null) }

        fun handleEvent(event: RecordEditUiEvent) {
            when (event) {
                RecordEditUiEvent.OnCloseClick -> {
                    navigator.pop()
                }

                RecordEditUiEvent.OnClearClick -> {
                    recordPageState.clearText()
                }

                RecordEditUiEvent.OnEmotionEditClick -> {
                    navigator.goTo(EmotionEditScreen)
                }
            }
        }

        return RecordEditUiState(
            recordInfo = recordInfo,
            recordPageState = recordPageState,
            recordQuoteState = recordQuoteState,
            recordImpressionState = recordImpressionState,
            isPageError = isPageError,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(RecordEditScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: RecordEditScreen,
            navigator: Navigator,
        ): RecordEditPresenter
    }

    companion object {
        const val MAX_PAGE = 4032
    }
}
