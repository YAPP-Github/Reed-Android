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
import kotlinx.collections.immutable.toPersistentList

class RecordRegisterPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RecordRegisterUiState> {

    @Composable
    override fun present(): RecordRegisterUiState {
        var currentStep by rememberRetained { mutableStateOf(RecordStep.QUOTE) }
        val recordPageState = rememberTextFieldState()
        val recordSentenceState = rememberTextFieldState()
        val impressionGuideList by rememberRetained {
            mutableStateOf(
                listOf(
                    "에서 위로 받았다",
                    "이 마음에 남았다",
                    "에서 작가의 의도가 궁금하다",
                    "에 대한 다른 사람들의 생각이 궁금하다",
                    "에서 크게 공감이 된다",
                    "을 보고 예전 기억이 났다",
                    "에서 문장에 머물렀다",
                ).toPersistentList(),
            )
        }
        var selectedImpressionGuide by rememberRetained { mutableStateOf("") }
        val impressionState = rememberTextFieldState()
        var isImpressionGuideBottomSheetVisible by rememberRetained { mutableStateOf(false) }
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

                is RecordRegisterUiEvent.OnSelectEmotion -> {

                }

                is RecordRegisterUiEvent.OnImpressionGuideButtonClick -> {
                    selectedImpressionGuide = ""
                    impressionState.edit {
                        replace(0, length, "")
                    }
                    isImpressionGuideBottomSheetVisible = true
                }

                is RecordRegisterUiEvent.OnSelectImpressionGuide -> {
                    val index = event.index
                    if (index in impressionGuideList.indices) {
                        selectedImpressionGuide = impressionGuideList[index]
                        impressionState.edit {
                            replace(0, length, "")
                            append(selectedImpressionGuide)
                        }
                    }
                }

                is RecordRegisterUiEvent.OnSelectionConfirmed -> {

                }

                is RecordRegisterUiEvent.OnImpressionGuideBottomSheetDismiss -> {
                    isImpressionGuideBottomSheetVisible = false
                }

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
            impressionGuideList = impressionGuideList,
            isImpressionGuideBottomSheetVisible = isImpressionGuideBottomSheetVisible,
            isExitDialogVisible = isExitDialogVisible,
            selectedImpressionGuide = selectedImpressionGuide,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): RecordRegisterPresenter
    }
}
