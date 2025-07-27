package com.ninecraft.booket.feature.record.register

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.designsystem.RecordStep
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
) : Presenter<RecordRegisterUiState> {

    @Composable
    override fun present(): RecordRegisterUiState {
        var currentStep by rememberRetained { mutableStateOf(RecordStep.QUOTE) }
        val recordPageState = rememberTextFieldState()
        val recordSentenceState = rememberTextFieldState()
        val impressionState = rememberTextFieldState()
        var isExitDialogVisible by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: RecordRegisterUiEvent) {
            when (event) {
                is RecordRegisterUiEvent.OnBackButtonClick -> {
                    when (currentStep) {
                        RecordStep.QUOTE -> {
                            isExitDialogVisible = true
                        }

                        RecordStep.EMOTION -> {
                            currentStep = RecordStep.QUOTE
                        }

                        RecordStep.IMPRESSION -> {
                            currentStep = RecordStep.EMOTION
                        }
                    }
                }

                is RecordRegisterUiEvent.OnClearClick -> {
                    recordPageState.clearText()
                }

                is RecordRegisterUiEvent.OnExitDialogConfirm -> {
                    navigator.pop()
                }

                is RecordRegisterUiEvent.OnExitDialogDismiss -> {
                    isExitDialogVisible = false
                }

                is RecordRegisterUiEvent.OnSentenceScanButtonClick -> {}
                is RecordRegisterUiEvent.OnNextButtonClick -> {
                    when (currentStep) {
                        RecordStep.QUOTE -> {
                            currentStep = RecordStep.EMOTION
                        }

                        RecordStep.EMOTION -> {
                            currentStep = RecordStep.IMPRESSION
                        }

                        RecordStep.IMPRESSION -> {
                            // TODO: (기록 완료 API 성공 시) 기록 상세 화면 이동
                        }
                    }
                }
            }
        }

        return RecordRegisterUiState(
            currentStep = currentStep,
            recordPageState = recordPageState,
            recordSentenceState = recordSentenceState,
            impressionState = impressionState,
            isExitDialogVisible = isExitDialogVisible,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): RecordRegisterPresenter
    }
}
