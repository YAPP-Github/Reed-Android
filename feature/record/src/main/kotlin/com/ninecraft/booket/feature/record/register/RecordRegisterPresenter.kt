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
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.model.Emotion
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
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@AssistedInject
class RecordRegisterPresenter(
    @Assisted private val screen: RecordScreen,
    @Assisted private val navigator: Navigator,
    private val repository: RecordRepository,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<RecordRegisterUiState> {

    @CircuitInject(RecordScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: RecordScreen, navigator: Navigator): RecordRegisterPresenter
    }

    companion object {
        private const val MAX_PAGE = 4032
        private const val RECORD_INPUT_SENTENCE = "record_input_sentence"
        private const val RECORD_SELECT_EMOTION = "record_select_emotion"
        private const val RECORD_INPUT_OPINION = "record_input_opinion"
        private const val RECORD_INPUT_HELP = "record_input_help"
        private const val RECORD_COMPLETE = "record_complete"
        private const val RECORD_DETAIL = "record_detail"
        private const val ERROR_RECORD_SAVE = "error_record_save"
    }

    @Composable
    override fun present(): RecordRegisterUiState {
        /** 2차 고도화 삭제 예정 ===================================================================== */
        val impressionState = rememberTextFieldState()
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
        var beforeSelectedImpressionGuide by rememberRetained { mutableStateOf(selectedImpressionGuide) }
        var isImpressionGuideBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isScanTooltipVisible by rememberRetained { mutableStateOf(true) }
        var isImpressionGuideTooltipVisible by rememberRetained { mutableStateOf(true) }

        /** ====================================================================================== */
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<RecordRegisterSideEffect?>(null) }
        var currentStep by rememberRetained { mutableStateOf(RecordStep.QUOTE) }
        val recordPageState = rememberTextFieldState()
        val recordSentenceState = rememberTextFieldState()
        val memoState = rememberTextFieldState()
        val emotions by rememberRetained { mutableStateOf(Emotion.entries.toPersistentList()) }
        var emotionDetails by rememberRetained { mutableStateOf(persistentListOf<String>()) }
        var selectedEmotion by rememberRetained { mutableStateOf<Emotion?>(null) }
        var selectedEmotionDetails by rememberRetained { mutableStateOf<Map<Emotion, ImmutableList<String>>>(emptyMap()) }
        var committedEmotion by rememberRetained { mutableStateOf<Emotion?>(null) }
        var committedEmotionDetails by rememberRetained { mutableStateOf<Map<Emotion, ImmutableList<String>>>(emptyMap()) }
        var isEmotionDetailBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var savedRecordId by rememberRetained { mutableStateOf("") }
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
                        recordSentenceState.text.isNotEmpty() && !isPageError
                    }

                    RecordStep.EMOTION -> {
                        committedEmotion != null
                    }

                    RecordStep.IMPRESSION -> true
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
                try {
                    isLoading = true
                    repository.postRecord(
                        userBookId = userBookId,
                        pageNumber = pageNumber,
                        quote = quote,
                        emotionTags = emotionTags,
                        review = impression,
                    ).onSuccess { result ->
                        analyticsHelper.logEvent(RECORD_COMPLETE)
                        savedRecordId = result.id
                        isRecordSavedDialogVisible = true
                    }.onFailure { exception ->
                        analyticsHelper.logEvent(ERROR_RECORD_SAVE)
                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
                            sideEffect = RecordRegisterSideEffect.ShowToast(message)
                        }

                        handleException(
                            exception = exception,
                            onError = handleErrorMessage,
                            onLoginRequired = {
                                navigator.resetRoot(LoginScreen())
                            },
                        )
                    }
                } finally {
                    isLoading = false
                }
            }
        }

        fun provideEmotionDetailMap(): Map<Emotion, ImmutableList<String>> {
            return mapOf(
                Emotion.WARM to persistentListOf("위로받은", "포근한", "다정한", "고마운", "마음이 놓이는", "편안한"),
                Emotion.JOY to persistentListOf("설레는", "뿌듯한", "유쾌한", "기쁜", "흥미진진한"),
                Emotion.SAD to persistentListOf("허무함", "외로운", "아쉬운", "먹먹한", "애틋한", "안타까운", "그리운"),
                Emotion.INSIGHT to persistentListOf("감탄한", "통찰력을 얻은", "영감을 받은", "생각이 깊어진", "새롭게 이해한"),
            )
        }

        fun getEmotionDetails(emotion: Emotion): ImmutableList<String> {
            return provideEmotionDetailMap()[emotion] ?: persistentListOf()
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
                    isScanTooltipVisible = false
                    ocrNavigator.goTo(OcrScreen)
                }

                is RecordRegisterUiEvent.OnSelectEmotion -> {
                    selectedEmotion = event.emotion
                }

                is RecordRegisterUiEvent.OnSelectEmotionV2 -> {
                    selectedEmotion = event.emotion
                    emotionDetails = getEmotionDetails(event.emotion).toPersistentList()
                    isEmotionDetailBottomSheetVisible = true
                }

                is RecordRegisterUiEvent.OnEmotionDetailToggled -> {
                    val emotionKey = selectedEmotion ?: return
                    val currentDetails = selectedEmotionDetails[selectedEmotion].orEmpty()
                    val updatedDetails = if (event.detail in currentDetails) {
                        currentDetails - event.detail
                    } else {
                        currentDetails + event.detail
                    }

                    selectedEmotionDetails = selectedEmotionDetails + (emotionKey to updatedDetails.toPersistentList())
                }

                is RecordRegisterUiEvent.OnEmotionDetailRemoved -> {
                    val emotionKey = selectedEmotion ?: return
                    val currentDetails = committedEmotionDetails[selectedEmotion].orEmpty()
                    val updatedDetails = currentDetails - event.detail

                    committedEmotionDetails = committedEmotionDetails + (emotionKey to updatedDetails.toPersistentList())
                    selectedEmotionDetails = selectedEmotionDetails + (emotionKey to updatedDetails.toPersistentList())
                }

                is RecordRegisterUiEvent.OnEmotionDetailSkipped -> {
                    committedEmotion = selectedEmotion
                    // 건너뛰기 시 세부감정 선택 초기화
                    committedEmotionDetails = persistentMapOf()
                    selectedEmotionDetails = persistentMapOf()
                    isEmotionDetailBottomSheetVisible = false
                }

                is RecordRegisterUiEvent.OnEmotionDetailCommitted -> {
                    val emotionKey = selectedEmotion ?: return
                    val details = selectedEmotionDetails[emotionKey] ?: persistentListOf()

                    committedEmotion = emotionKey
                    committedEmotionDetails = mapOf(emotionKey to details)
                    selectedEmotionDetails = mapOf(emotionKey to details)
                    isEmotionDetailBottomSheetVisible = false
                }

                is RecordRegisterUiEvent.OnEmotionDetailBottomSheetDismiss -> {
                    isEmotionDetailBottomSheetVisible = false
                }

                /** 2차 고도화 삭제 예정 ===================================================================== */
                is RecordRegisterUiEvent.OnImpressionGuideButtonClick -> {
                    analyticsHelper.logScreenView(RECORD_INPUT_HELP)
                    isImpressionGuideTooltipVisible = false
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
                /** ====================================================================================== */

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
                                emotionTags = selectedEmotion?.let { listOf(it.displayName) } ?: emptyList(),
                                impression = impressionState.text.toString(),
                            )
                        }
                    }
                }

                is RecordRegisterUiEvent.OnRecordSavedDialogConfirm -> {
                    analyticsHelper.logScreenView(RECORD_DETAIL)
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

        ImpressionEffect(currentStep) {
            val screenName = when (currentStep) {
                RecordStep.QUOTE -> RECORD_INPUT_SENTENCE
                RecordStep.EMOTION -> RECORD_SELECT_EMOTION
                RecordStep.IMPRESSION -> RECORD_INPUT_OPINION
            }
            analyticsHelper.logScreenView(screenName)
        }

        return RecordRegisterUiState(
            isLoading = isLoading,
            currentStep = currentStep,
            recordPageState = recordPageState,
            recordSentenceState = recordSentenceState,
            memoState = memoState,
            isPageError = isPageError,
            emotions = emotions,
            emotionDetails = emotionDetails,
            selectedEmotion = selectedEmotion,
            selectedEmotionDetails = selectedEmotionDetails,
            committedEmotion = committedEmotion,
            committedEmotionDetails = committedEmotionDetails,
            isEmotionDetailBottomSheetVisible = isEmotionDetailBottomSheetVisible,
            impressionState = impressionState,
            impressionGuideList = impressionGuideList,
            selectedImpressionGuide = selectedImpressionGuide,
            beforeSelectedImpressionGuide = beforeSelectedImpressionGuide,
            savedRecordId = savedRecordId,
            isNextButtonEnabled = isNextButtonEnabled,
            isImpressionGuideBottomSheetVisible = isImpressionGuideBottomSheetVisible,
            isExitDialogVisible = isExitDialogVisible,
            isRecordSavedDialogVisible = isRecordSavedDialogVisible,
            isScanTooltipVisible = isScanTooltipVisible,
            isImpressionGuideTooltipVisible = isImpressionGuideTooltipVisible,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
