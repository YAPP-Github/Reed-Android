package com.ninecraft.booket.feature.record.register

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.designsystem.EmotionTag
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.feature.screens.OcrScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.ReviewDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class RecordRegisterPresenter @AssistedInject constructor(
    @Assisted private val screen: RecordScreen,
    @Assisted private val navigator: Navigator,
    private val repository: RecordRepository,
) : Presenter<RecordRegisterUiState> {

    @Composable
    override fun present(): RecordRegisterUiState {
        val scope = rememberCoroutineScope()

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
        val emotionTags by rememberRetained { mutableStateOf(EmotionTag.entries.toPersistentList()) }
        var selectedEmotion by rememberRetained { mutableStateOf<EmotionTag?>(null) }
        var selectedImpressionGuide by rememberRetained { mutableStateOf("") }
        val impressionState = rememberTextFieldState()
        var isImpressionGuideBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isExitDialogVisible by rememberRetained { mutableStateOf(false) }
        var isRecordSavedDialogVisible by rememberRetained { mutableStateOf(false) }
        var isNextButtonEnabled by rememberRetained { mutableStateOf(false) }

        val ocrNavigator = rememberAnsweringNavigator<OcrScreen.OcrResult>(navigator) { result ->
            recordSentenceState.edit {
                replace(0, length, "")
                append(result.sentence)
            }
        }

        fun postRecord(
            userBookId: String,
            pageNumber: Int,
            quote: String,
            emotionTags: List<String>,
            impression: String,
        ) {
            scope.launch {
                repository.postRecord(
                    userBookId = userBookId,
                    pageNumber = pageNumber,
                    quote = quote,
                    emotionTags = emotionTags,
                    review = impression,
                ).onSuccess {
                    isRecordSavedDialogVisible = true
                }.onFailure {
                    // TODO: 등록 실패 다이얼로그 띄우기
                }
            }
        }

        fun updateIsNextButtonEnabled() {
            isNextButtonEnabled = when (currentStep) {
                RecordStep.QUOTE -> {
                    recordPageState.text.isNotEmpty() && recordSentenceState.text.isNotEmpty()
                }

                RecordStep.EMOTION -> {
                    selectedEmotion != null
                }

                RecordStep.IMPRESSION -> {
                    impressionState.text.isNotEmpty()
                }
            }
        }

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
                    isExitDialogVisible = false
                    navigator.pop()
                }

                is RecordRegisterUiEvent.OnExitDialogDismiss -> {
                    isExitDialogVisible = false
                }

                is RecordRegisterUiEvent.OnSentenceScanButtonClick -> {
                    ocrNavigator.goTo(OcrScreen)
                }

                is RecordRegisterUiEvent.OnSelectEmotion -> {
                    selectedEmotion = event.emotion
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

                is RecordRegisterUiEvent.OnSelectionConfirmed -> {}

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
                            postRecord(
                                userBookId = screen.userBookId,
                                pageNumber = recordPageState.text.toString().toIntOrNull() ?: 0,
                                quote = recordSentenceState.text.toString(),
                                emotionTags = selectedEmotion?.let { listOf(it.label) } ?: emptyList(),
                                impression = impressionState.text.toString(),
                            )
                        }
                    }
                }

                is RecordRegisterUiEvent.OnRecordSavedDialogConfirm -> {
                    isRecordSavedDialogVisible = false
                    navigator.pop()
                    navigator.goTo(ReviewDetailScreen)
                }

                is RecordRegisterUiEvent.OnRecordSavedDialogDismiss -> {
                    isRecordSavedDialogVisible = false
                    navigator.pop()
                }
            }
        }

        LaunchedEffect(
            currentStep,
            recordPageState.text,
            recordSentenceState.text,
            selectedEmotion,
            impressionState.text
        ) {
            updateIsNextButtonEnabled()
        }

        return RecordRegisterUiState(
            currentStep = currentStep,
            recordPageState = recordPageState,
            recordSentenceState = recordSentenceState,
            emotionTags = emotionTags,
            selectedEmotion = selectedEmotion,
            impressionState = impressionState,
            impressionGuideList = impressionGuideList,
            selectedImpressionGuide = selectedImpressionGuide,
            isNextButtonEnabled = isNextButtonEnabled,
            isImpressionGuideBottomSheetVisible = isImpressionGuideBottomSheetVisible,
            isExitDialogVisible = isExitDialogVisible,
            isRecordSavedDialogVisible = isRecordSavedDialogVisible,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: RecordScreen,
            navigator: Navigator,
        ): RecordRegisterPresenter
    }
}
