package com.ninecraft.booket.feature.edit.record

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.feature.screens.EmotionEditScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.RecordEditScreen
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
import kotlinx.coroutines.launch

@AssistedInject
class RecordEditPresenter(
    @Assisted private val screen: RecordEditScreen,
    @Assisted private val navigator: Navigator,
    private val repository: RecordRepository,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<RecordEditUiState> {

    @CircuitInject(RecordEditScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: RecordEditScreen, navigator: Navigator): RecordEditPresenter
    }

    companion object {
        private const val MAX_PAGE = 4032
        private const val RECORD_EDIT = "record_edit_save"
        private const val RECORD_EDIT_SAVE = "record_edit_save"
    }

    @Composable
    override fun present(): RecordEditUiState {
        val scope = rememberCoroutineScope()
        var recordInfo by rememberRetained { mutableStateOf(screen.recordInfo) }
        val recordPageState = rememberTextFieldState(recordInfo.pageNumber.toString())
        val recordQuoteState = rememberTextFieldState(recordInfo.quote)
        val recordImpressionState = rememberTextFieldState(recordInfo.review)
        val isPageError by remember {
            derivedStateOf {
                val page = recordPageState.text.toString().toIntOrNull() ?: 0
                page > MAX_PAGE
            }
        }
        val hasChanges by remember {
            derivedStateOf {
                val pageChanged = recordPageState.text.toString() != recordInfo.pageNumber.toString()
                val quoteChanged = recordQuoteState.text.toString() != recordInfo.quote
                val impressionChanged = recordImpressionState.text.toString() != recordInfo.review

                val originalPrimaryEmotionCode = screen.recordInfo.primaryEmotion.code
                val originalDetailEmotionIds = screen.recordInfo.detailEmotions.map { it.id }.toSet()
                val currentPrimaryEmotionCode = recordInfo.primaryEmotion.code
                val currentDetailEmotionIds = recordInfo.detailEmotions.map { it.id }.toSet()
                val emotionChanged = originalPrimaryEmotionCode != currentPrimaryEmotionCode || originalDetailEmotionIds != currentDetailEmotionIds
                pageChanged || quoteChanged || impressionChanged || emotionChanged
            }
        }
        val isSaveButtonEnabled by remember {
            derivedStateOf {
                recordPageState.text.isNotEmpty() &&
                    recordQuoteState.text.isNotEmpty() &&
                    !isPageError &&
                    hasChanges
            }
        }
        var sideEffect by rememberRetained { mutableStateOf<RecordEditSideEffect?>(null) }

        val emotionEditNavigator = rememberAnsweringNavigator<EmotionEditScreen.Result>(navigator) { result ->
            recordInfo = recordInfo.copy(
                primaryEmotion = result.primaryEmotion,
                detailEmotions = result.detailEmotions,
            )
        }

        fun editRecord(
            readingRecordId: String,
            pageNumber: Int,
            quote: String,
            primaryEmotion: String,
            detailEmotionIds: List<String>,
            impression: String,
            onSuccess: () -> Unit = {},
        ) {
            scope.launch {
                repository.editRecord(
                    readingRecordId = readingRecordId,
                    pageNumber = pageNumber,
                    quote = quote,
                    review = impression,
                    primaryEmotion = primaryEmotion,
                    detailEmotionTagIds = detailEmotionIds,
                ).onSuccess {
                    analyticsHelper.logEvent(RECORD_EDIT_SAVE)
                    onSuccess()
                }.onFailure { exception ->
                    val handleErrorMessage = { message: String ->
                        Logger.e(message)
                        sideEffect = RecordEditSideEffect.ShowToast(message)
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

        fun handleEvent(event: RecordEditUiEvent) {
            when (event) {
                RecordEditUiEvent.OnCloseClick -> {
                    navigator.pop()
                }

                RecordEditUiEvent.OnClearClick -> {
                    recordPageState.clearText()
                }

                RecordEditUiEvent.OnEmotionEditClick -> {
                    emotionEditNavigator.goTo(
                        EmotionEditScreen(
                            primaryEmotionCode = recordInfo.primaryEmotion.code,
                            detailEmotionIds = recordInfo.detailEmotions.map { it.id },
                        ),
                    )
                }

                RecordEditUiEvent.OnSaveButtonClick -> {
                    editRecord(
                        readingRecordId = recordInfo.id,
                        pageNumber = recordPageState.text.toString().toIntOrNull() ?: 0,
                        quote = recordQuoteState.text.toString(),
                        primaryEmotion = recordInfo.primaryEmotion.code.name,
                        detailEmotionIds = recordInfo.detailEmotions.map { it.id },
                        impression = recordImpressionState.text.toString(),
                        onSuccess = {
                            navigator.pop()
                        },
                    )
                }
            }
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(RECORD_EDIT)
        }

        return RecordEditUiState(
            recordInfo = recordInfo,
            recordPageState = recordPageState,
            recordQuoteState = recordQuoteState,
            recordImpressionState = recordImpressionState,
            isPageError = isPageError,
            isSaveButtonEnabled = isSaveButtonEnabled,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
