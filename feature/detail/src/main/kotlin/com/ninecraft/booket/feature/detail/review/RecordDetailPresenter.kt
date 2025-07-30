package com.ninecraft.booket.feature.detail.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.RecordDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class RecordDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RecordDetailUiState> {

    @Suppress("unused")
    @Composable
    override fun present(): RecordDetailUiState {
        val scope = rememberCoroutineScope()

        var sideEffect by rememberRetained { mutableStateOf<RecordDetailSideEffect?>(null) }

        fun handleEvent(event: RecordDetailUiEvent) {
            when (event) {
                RecordDetailUiEvent.OnCloseClicked -> {
                    navigator.pop()
                }
            }
        }
        return RecordDetailUiState(
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

@CircuitInject(RecordDetailScreen::class, ActivityRetainedComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(navigator: Navigator): RecordDetailPresenter
}
