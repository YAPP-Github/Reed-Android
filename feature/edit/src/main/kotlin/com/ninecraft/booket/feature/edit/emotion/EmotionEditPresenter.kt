package com.ninecraft.booket.feature.edit.emotion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.designsystem.EmotionTag
import com.ninecraft.booket.feature.screens.EmotionEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toPersistentList

class EmotionEditPresenter @AssistedInject constructor(
    @Assisted private val screen: EmotionEditScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<EmotionEditUiState> {
    @Composable
    override fun present(): EmotionEditUiState {
        var selectedEmotion by rememberRetained { mutableStateOf(screen.emotion) }
        val emotionTags by rememberRetained { mutableStateOf(EmotionTag.entries.toPersistentList()) }
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
            emotionTags = emotionTags,
            isEditButtonEnabled = isEditButtonEnabled,
            eventSink = ::handleEvent,
        )
    }
}

@CircuitInject(EmotionEditScreen::class, ActivityRetainedComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(
        screen: EmotionEditScreen,
        navigator: Navigator,
    ): EmotionEditPresenter
}
