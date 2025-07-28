package com.ninecraft.booket.feature.detail.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.ReviewDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class ReviewDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<ReviewDetailUiState> {

    @Composable
    override fun present(): ReviewDetailUiState {
        val scope = rememberCoroutineScope()

        var sideEffect by rememberRetained { mutableStateOf<ReviewDetailSideEffect?>(null) }

        fun handleEvent(event: ReviewDetailUiEvent) {
            when (event) {
                ReviewDetailUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                ReviewDetailUiEvent.OnBackClicked -> {
                    navigator.pop()
                }
            }
        }
        return ReviewDetailUiState(
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

@CircuitInject(ReviewDetailScreen::class, ActivityRetainedComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(
        navigator: Navigator,
    ): ReviewDetailPresenter
}
