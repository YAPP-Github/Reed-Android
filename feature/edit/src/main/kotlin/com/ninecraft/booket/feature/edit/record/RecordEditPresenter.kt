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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class RecordEditPresenter @AssistedInject constructor(
    @Assisted private val screen: RecordEditScreen,
    @Assisted private val navigator: Navigator,
    private val repository: RecordRepository,
) : Presenter<RecordEditUiState> {

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
                val pageChanged = recordPageState.text != recordInfo.pageNumber.toString()
                val quoteChanged = recordQuoteState.text != recordInfo.quote
                val impressionChanged = recordImpressionState.text != recordInfo.review
                val emotionChanged = recordInfo.emotionTags != screen.recordInfo.emotionTags
                pageChanged || quoteChanged || impressionChanged || emotionChanged
            }
        }
        val isSaveButtonEnabled by remember {
            derivedStateOf {
                recordPageState.text.isNotEmpty() &&
                    recordQuoteState.text.isNotEmpty() &&
                    recordImpressionState.text.isNotEmpty() &&
                    !isPageError &&
                    hasChanges
            }
        }
        var sideEffect by rememberRetained { mutableStateOf<RecordEditSideEffect?>(null) }

        val emotionEditNavigator = rememberAnsweringNavigator<EmotionEditScreen.Result>(navigator) { result ->
            recordInfo = recordInfo.copy(emotionTags = listOf(result.emotion))
        }

        fun editRecord(
            readingRecordId: String,
            userBookId: String,
            pageNumber: Int,
            quote: String,
            emotionTags: List<String>,
            impression: String,
            onSuccess: () -> Unit = {},
        ) {
            scope.launch {
                repository.editRecord(
                    readingRecordId = readingRecordId,
                    userBookId = userBookId,
                    pageNumber = pageNumber,
                    quote = quote,
                    emotionTags = emotionTags,
                    review = impression,
                ).onSuccess {
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
                            navigator.resetRoot(LoginScreen)
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
                    val emotion = recordInfo.emotionTags.firstOrNull() ?: ""
                    emotionEditNavigator.goTo(EmotionEditScreen(emotion))
                }

                RecordEditUiEvent.OnSaveButtonClick -> {
                    editRecord(
                        readingRecordId = recordInfo.id,
                        userBookId = recordInfo.userBookId,
                        pageNumber = recordPageState.text.toString().toIntOrNull() ?: 0,
                        quote = recordQuoteState.text.toString(),
                        emotionTags = recordInfo.emotionTags,
                        impression = recordImpressionState.text.toString(),
                        onSuccess = {
                            navigator.pop()
                        },
                    )
                }
            }
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

    @CircuitInject(RecordEditScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: RecordEditScreen,
            navigator: Navigator,
        ): RecordEditPresenter
    }

    companion object {
        const val MAX_PAGE = 4032
    }
}
