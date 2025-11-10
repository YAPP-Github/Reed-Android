package com.ninecraft.booket.feature.detail.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.feature.screens.RecordCardScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.ImpressionEffect
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Inject

@Inject
@CircuitInject(RecordCardScreen::class, AppScope::class)
class RecordCardPresenter(
    @Assisted private val screen: RecordCardScreen,
    @Assisted private val navigator: Navigator,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<RecordCardUiState> {

    companion object {
        private const val RECORD_CARD_SAVE = "record_card_save"
        private const val RECORD_CARD_SHARE = "record_card_share"
    }

    @Composable
    override fun present(): RecordCardUiState {
        var isLoading by rememberRetained { mutableStateOf(false) }
        var isCapturing by rememberRetained { mutableStateOf(false) }
        var isSharing by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<RecordCardSideEffect?>(null) }

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

                is RecordCardUiEvent.SaveRecordCard -> {
                    isCapturing = false
                    analyticsHelper.logEvent(RECORD_CARD_SAVE)
                    sideEffect = RecordCardSideEffect.SaveImage(event.bitmap)
                }

                is RecordCardUiEvent.ShareRecordCard -> {
                    isSharing = false
                    analyticsHelper.logEvent(RECORD_CARD_SHARE)
                    sideEffect = RecordCardSideEffect.ShareImage(event.bitmap)
                }
            }
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(screen.name)
        }

        return RecordCardUiState(
            isLoading = isLoading,
            quote = screen.quote,
            bookTitle = screen.bookTitle,
            emotion = screen.emotion,
            isCapturing = isCapturing,
            isSharing = isSharing,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
