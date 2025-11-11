package com.ninecraft.booket.feature.edit.emotion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.feature.screens.EmotionEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.toPersistentList

@Inject
class EmotionEditPresenter(
    @Assisted private val screen: EmotionEditScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<EmotionEditUiState> {

    @CircuitInject(EmotionEditScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: EmotionEditScreen, navigator: Navigator): EmotionEditPresenter
    }

    @Composable
    override fun present(): EmotionEditUiState {
        var selectedEmotion by rememberRetained { mutableStateOf(screen.emotion) }
        val emotions by rememberRetained { mutableStateOf(Emotion.entries.toPersistentList()) }
        val isEditButtonEnabled by remember {
            derivedStateOf {
                selectedEmotion != screen.emotion
            }
        }

        fun handleEvent(event: EmotionEditUiEvent) {
            when (event) {
                is EmotionEditUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is EmotionEditUiEvent.OnSelectEmotion -> {
                    selectedEmotion = event.emotion
                }

                is EmotionEditUiEvent.OnEditButtonClick -> {
                    navigator.pop(result = EmotionEditScreen.Result(selectedEmotion))
                }
            }
        }

        return EmotionEditUiState(
            selectedEmotion = selectedEmotion,
            emotions = emotions,
            isEditButtonEnabled = isEditButtonEnabled,
            eventSink = ::handleEvent,
        )
    }
}
