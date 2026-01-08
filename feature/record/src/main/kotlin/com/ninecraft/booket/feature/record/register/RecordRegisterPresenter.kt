package com.ninecraft.booket.feature.record.register

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.EmotionRepository
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionGroupModel
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
    private val emotionRepository: EmotionRepository,
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
        private const val RECORD_COMPLETE = "record_complete"
        private const val RECORD_DETAIL = "record_detail"
        private const val ERROR_RECORD_SAVE = "error_record_save"
    }

    @Composable
    override fun present(): RecordRegisterUiState {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<RecordRegisterSideEffect?>(null) }
        var currentStep by rememberRetained { mutableStateOf(RecordStep.QUOTE) }
        val recordPageState = rememberTextFieldState()
        val recordSentenceState = rememberTextFieldState()
        val memoState = rememberTextFieldState()
        var emotionGroups by rememberRetained { mutableStateOf(persistentListOf<EmotionGroupModel>()) }
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
            primaryEmotion: String,
            detailEmotionTagIds: List<String>,
            review: String,
        ) {
            scope.launch {
                try {
                    isLoading = true
                    repository.postRecord(
                        userBookId = userBookId,
                        pageNumber = pageNumber,
                        quote = quote,
                        review = review,
                        primaryEmotion = primaryEmotion,
                        detailEmotionTagIds = detailEmotionTagIds,
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

        fun getEmotionGroups() {
            scope.launch {
                emotionRepository.getEmotions()
                    .onSuccess { result ->
                        emotionGroups = result.emotions.toPersistentList()
                    }.onFailure { exception ->
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

                is RecordRegisterUiEvent.OnNextButtonClick -> {
                    when (currentStep) {
                        RecordStep.QUOTE -> {
                            currentStep = RecordStep.EMOTION
                        }

                        RecordStep.EMOTION -> {
                            postRecord(
                                userBookId = screen.userBookId,
                                pageNumber = recordPageState.text.toString().toIntOrNull() ?: 0,
                                quote = recordSentenceState.text.toString(),
                                review = memoState.text.toString(),
                                primaryEmotion = "",
                                detailEmotionTagIds = emptyList(),
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
            emotionGroups = emotionGroups,
            emotions = emotions,
            emotionDetails = emotionDetails,
            selectedEmotion = selectedEmotion,
            selectedEmotionDetails = selectedEmotionDetails,
            committedEmotion = committedEmotion,
            committedEmotionDetails = committedEmotionDetails,
            isEmotionDetailBottomSheetVisible = isEmotionDetailBottomSheetVisible,
            savedRecordId = savedRecordId,
            isNextButtonEnabled = isNextButtonEnabled,
            isExitDialogVisible = isExitDialogVisible,
            isRecordSavedDialogVisible = isRecordSavedDialogVisible,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
