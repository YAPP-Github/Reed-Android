package com.ninecraft.booket.feature.record.register

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.common.constants.ErrorScope
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.common.utils.postErrorDialog
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.designsystem.EmotionTag
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.OcrScreen
import com.ninecraft.booket.feature.screens.RecordDetailScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.extensions.delayedPop
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.ImpressionEffect
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
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<RecordRegisterUiState> {

    @Composable
    override fun present(): RecordRegisterUiState {
        val scope = rememberCoroutineScope()
        var sideEffect by rememberRetained { mutableStateOf<RecordRegisterSideEffect?>(null) }
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
        var beforeSelectedImpressionGuide by rememberRetained { mutableStateOf(selectedImpressionGuide) }
        val impressionState = rememberTextFieldState()
        var savedRecordId by rememberRetained { mutableStateOf("") }
        var isImpressionGuideBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isExitDialogVisible by rememberRetained { mutableStateOf(false) }
        var isRecordSavedDialogVisible by rememberRetained { mutableStateOf(false) }
        val isPageError by remember {
            derivedStateOf {
                val page = recordPageState.text.toString().toIntOrNull() ?: 0
                page > MAX_PAGE
            }
        }
        val isNextButtonEnabled by remember {
            derivedStateOf {
                when (currentStep) {
                    RecordStep.QUOTE -> {
                        recordPageState.text.isNotEmpty() && recordSentenceState.text.isNotEmpty() && !isPageError
                    }

                    RecordStep.EMOTION -> {
                        selectedEmotion != null
                    }

                    RecordStep.IMPRESSION -> {
                        impressionState.text.isNotEmpty()
                    }
                }
            }
        }

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
                ).onSuccess { result ->
                    savedRecordId = result.id
                    isRecordSavedDialogVisible = true
                }.onFailure { exception ->
                    postErrorDialog(
                        errorScope = ErrorScope.RECORD_REGISTER,
                        exception = exception,
                    )

                    val handleErrorMessage = { message: String ->
                        Logger.e(message)
                        sideEffect = RecordRegisterSideEffect.ShowToast(message)
                    }

                    handleException(
                        exception = exception,
                        onError = handleErrorMessage,
                        onLoginRequired = {
                            navigator.resetRoot(LoginScreen)
                        },
                    )
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
                    scope.launch {
                        navigator.delayedPop()
                    }
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
                    beforeSelectedImpressionGuide = selectedImpressionGuide
                    if (impressionState.text.isEmpty()) {
                        selectedImpressionGuide = ""
                    }
                    isImpressionGuideBottomSheetVisible = true
                }

                is RecordRegisterUiEvent.OnSelectImpressionGuide -> {
                    val index = event.index
                    if (index in impressionGuideList.indices) {
                        selectedImpressionGuide = impressionGuideList[index]
                    }
                }

                is RecordRegisterUiEvent.OnImpressionGuideConfirmed -> {
                    val currentImpressionText = impressionState.text.toString()

                    if (currentImpressionText.isNotEmpty()) {
                        // 이미 작성된 감상문이 있는 경우 줄바꿈해서 추가
                        val startIndex = currentImpressionText.length

                        impressionState.edit {
                            replace(0, length, currentImpressionText + "\n" + selectedImpressionGuide)
                            this.selection = TextRange(startIndex + 1) // 줄바꿈한 문장 맨 앞에 커서 위치
                        }
                    } else {
                        impressionState.edit {
                            replace(0, length, "")
                            append(selectedImpressionGuide)
                            this.selection = TextRange(0) // 커서를 문장 맨 앞에 위치
                        }
                    }

                    isImpressionGuideBottomSheetVisible = false
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
                    navigator.goTo(RecordDetailScreen(event.recordId))
                }

                is RecordRegisterUiEvent.OnRecordSavedDialogDismiss -> {
                    isRecordSavedDialogVisible = false
                    scope.launch {
                        navigator.delayedPop()
                    }
                }
            }
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(screen.name)
        }

        return RecordRegisterUiState(
            currentStep = currentStep,
            recordPageState = recordPageState,
            recordSentenceState = recordSentenceState,
            isPageError = isPageError,
            emotionTags = emotionTags,
            selectedEmotion = selectedEmotion,
            impressionState = impressionState,
            impressionGuideList = impressionGuideList,
            selectedImpressionGuide = selectedImpressionGuide,
            beforeSelectedImpressionGuide = beforeSelectedImpressionGuide,
            savedRecordId = savedRecordId,
            isNextButtonEnabled = isNextButtonEnabled,
            isImpressionGuideBottomSheetVisible = isImpressionGuideBottomSheetVisible,
            isExitDialogVisible = isExitDialogVisible,
            isRecordSavedDialogVisible = isRecordSavedDialogVisible,
            sideEffect = sideEffect,
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

    companion object {
        const val MAX_PAGE = 4032
    }
}
