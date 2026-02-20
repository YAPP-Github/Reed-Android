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
import com.ninecraft.booket.core.model.EmotionCode
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
import kotlinx.collections.immutable.PersistentMap
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
        var emotionUiState by rememberRetained { mutableStateOf<EmotionUiState>(EmotionUiState.Idle) }
        var sideEffect by rememberRetained { mutableStateOf<RecordRegisterSideEffect?>(null) }
        var currentStep by rememberRetained { mutableStateOf(RecordStep.QUOTE) }
        val recordPageState = rememberTextFieldState()
        val recordSentenceState = rememberTextFieldState()
        val memoState = rememberTextFieldState()
        var emotionGroups by rememberRetained { mutableStateOf(persistentListOf<EmotionGroupModel>()) }
        var pendingEmotionCode by rememberRetained { mutableStateOf<EmotionCode?>(null) }
        var selectedEmotionCode by rememberRetained { mutableStateOf<EmotionCode?>(null) }
        var selectedEmotionMap by rememberRetained { mutableStateOf<PersistentMap<EmotionCode, ImmutableList<String>>>(persistentMapOf()) }
        var committedEmotionCode by rememberRetained { mutableStateOf<EmotionCode?>(null) }
        var committedEmotionMap by rememberRetained { mutableStateOf<PersistentMap<EmotionCode, ImmutableList<String>>>(persistentMapOf()) }
        var isEmotionDetailBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var savedRecordId by rememberRetained { mutableStateOf("") }
        var isExitDialogVisible by rememberRetained { mutableStateOf(false) }
        var isEmotionEditDialogVisible by rememberRetained { mutableStateOf(false) }
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
                        committedEmotionCode != null
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
            pageNumber: Int?,
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

        fun getEmotionGroups() {
            scope.launch {
                emotionUiState = EmotionUiState.Loading
                emotionRepository.getEmotions()
                    .onSuccess { result ->
                        emotionUiState = EmotionUiState.Success
                        emotionGroups = result.emotions.toPersistentList()
                    }.onFailure { exception ->
                        emotionUiState = EmotionUiState.Error(exception)

                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
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

                is RecordRegisterUiEvent.OnSelectEmotionCode -> {
                    if (selectedEmotionCode != null && selectedEmotionCode != event.emotionCode) {
                        pendingEmotionCode = event.emotionCode
                        isEmotionEditDialogVisible = true
                    } else {
                        selectedEmotionCode = event.emotionCode

                        if (selectedEmotionCode == EmotionCode.OTHER) {
                            committedEmotionCode = selectedEmotionCode
                            committedEmotionMap = persistentMapOf()
                            selectedEmotionMap = persistentMapOf()
                        } else {
                            isEmotionDetailBottomSheetVisible = true
                        }
                    }
                }

                is RecordRegisterUiEvent.OnEmotionDetailToggled -> {
                    val emotionKey = selectedEmotionCode ?: return
                    val currentDetails = selectedEmotionMap[selectedEmotionCode].orEmpty()
                    val updatedDetails = if (event.detailId in currentDetails) {
                        currentDetails - event.detailId
                    } else {
                        currentDetails + event.detailId
                    }

                    selectedEmotionMap = selectedEmotionMap.put(emotionKey, updatedDetails.toPersistentList())
                }

                is RecordRegisterUiEvent.OnEmotionDetailRemoved -> {
                    val emotionKey = selectedEmotionCode ?: return
                    val currentDetails = committedEmotionMap[selectedEmotionCode].orEmpty()
                    val updatedDetails = currentDetails - event.detailId

                    committedEmotionMap = committedEmotionMap.put(emotionKey, updatedDetails.toPersistentList())
                    selectedEmotionMap = selectedEmotionMap.put(emotionKey, updatedDetails.toPersistentList())
                }

                is RecordRegisterUiEvent.OnEmotionDetailSkipped -> {
                    committedEmotionCode = selectedEmotionCode
                    // 건너뛰기 시 세부감정 선택 초기화
                    committedEmotionMap = persistentMapOf()
                    selectedEmotionMap = persistentMapOf()
                    isEmotionDetailBottomSheetVisible = false
                }

                is RecordRegisterUiEvent.OnEmotionDetailCommitted -> {
                    val emotionKey = selectedEmotionCode ?: return
                    val details = selectedEmotionMap[emotionKey] ?: persistentListOf()

                    committedEmotionCode = emotionKey
                    committedEmotionMap = persistentMapOf(emotionKey to details)
                    selectedEmotionMap = persistentMapOf(emotionKey to details)
                    isEmotionDetailBottomSheetVisible = false
                }

                is RecordRegisterUiEvent.OnEmotionDetailBottomSheetDismiss -> {
                    if (committedEmotionCode == null) {
                        selectedEmotionCode = null
                        selectedEmotionMap = persistentMapOf()
                    }
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
                                pageNumber = recordPageState.text.toString().toIntOrNull(),
                                quote = recordSentenceState.text.toString(),
                                review = memoState.text.toString(),
                                primaryEmotion = committedEmotionCode?.name ?: "",
                                detailEmotionTagIds = committedEmotionMap[committedEmotionCode] ?: persistentListOf(),
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

                RecordRegisterUiEvent.OnRetryGetEmotions -> {
                    getEmotionGroups()
                }

                RecordRegisterUiEvent.OnEmotionEditDialogConfirm -> {
                    selectedEmotionCode = pendingEmotionCode

                    if (selectedEmotionCode == EmotionCode.OTHER) {
                        committedEmotionCode = selectedEmotionCode
                        committedEmotionMap = persistentMapOf()
                        selectedEmotionMap = persistentMapOf()
                    } else {
                        isEmotionDetailBottomSheetVisible = true
                    }
                    isEmotionEditDialogVisible = false
                }

                RecordRegisterUiEvent.OnEmotionEditDialogDismiss -> {
                    isEmotionEditDialogVisible = false
                }
            }
        }

        LaunchedEffect(Unit) {
            getEmotionGroups()
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
            emotionUiState = emotionUiState,
            currentStep = currentStep,
            recordPageState = recordPageState,
            recordSentenceState = recordSentenceState,
            memoState = memoState,
            isPageError = isPageError,
            emotionGroups = emotionGroups,
            selectedEmotionCode = selectedEmotionCode,
            selectedEmotionMap = selectedEmotionMap,
            committedEmotionCode = committedEmotionCode,
            committedEmotionMap = committedEmotionMap,
            isEmotionDetailBottomSheetVisible = isEmotionDetailBottomSheetVisible,
            savedRecordId = savedRecordId,
            isNextButtonEnabled = isNextButtonEnabled,
            isExitDialogVisible = isExitDialogVisible,
            isEmotionEditDialogVisible = isEmotionEditDialogVisible,
            isRecordSavedDialogVisible = isRecordSavedDialogVisible,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
