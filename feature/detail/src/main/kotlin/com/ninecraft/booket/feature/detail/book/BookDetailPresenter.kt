package com.ninecraft.booket.feature.detail.book

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.RecordDetailScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class BookDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: BookDetailScreen,
    @Assisted private val navigator: Navigator,
    private val repository: BookRepository,
) : Presenter<BookDetailUiState> {

    @Composable
    override fun present(): BookDetailUiState {
        val scope = rememberCoroutineScope()

        var isLoading by rememberRetained { mutableStateOf(false) }
        var bookDetail by rememberRetained { mutableStateOf(BookDetailModel()) }
        var isBookUpdateBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isRecordSortBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var currentBookStatus by rememberRetained { mutableStateOf(BookStatus.READING) }
        var currentRecordSort by rememberRetained { mutableStateOf(RecordSort.PAGE_ASCENDING) }
        var sideEffect by rememberRetained { mutableStateOf<BookDetailSideEffect?>(null) }

        fun getBookDetail() {
            scope.launch {
                try {
                    isLoading = true
                    repository.getBookDetail(screen.bookId)
                        .onSuccess { result ->
                            bookDetail = result
                        }
                        .onFailure { exception ->
                            val handleErrorMessage = { message: String ->
                                Logger.e(message)
                                sideEffect = BookDetailSideEffect.ShowToast(message)
                            }

                            handleException(
                                exception = exception,
                                onError = handleErrorMessage,
                                onLoginRequired = {
                                    navigator.resetRoot(LoginScreen)
                                },
                            )
                        }
                } finally {
                    isLoading = false
                }
            }
        }

        fun upsertBook(bookIsbn: String, bookStatus: String) {
            scope.launch {
                repository.upsertBook(bookIsbn, bookStatus)
                    .onSuccess {
                        isBookUpdateBottomSheetVisible = false
                    }
                    .onFailure { exception ->
                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
                            sideEffect = BookDetailSideEffect.ShowToast(message)
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

        LaunchedEffect(screen.bookId) {
            getBookDetail()
        }

        fun handleEvent(event: BookDetailUiEvent) {
            when (event) {
                is BookDetailUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is BookDetailUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is BookDetailUiEvent.OnBookStatusButtonClick -> {
                    isBookUpdateBottomSheetVisible = true
                }

                is BookDetailUiEvent.OnRegisterRecordButtonClick -> {
                    navigator.goTo(RecordScreen(""))
                }

                is BookDetailUiEvent.OnRecordSortButtonClick -> {
                    isRecordSortBottomSheetVisible = true
                }

                is BookDetailUiEvent.OnBookUpdateBottomSheetDismiss -> {
                    isBookUpdateBottomSheetVisible = false
                }

                is BookDetailUiEvent.OnBookStatusItemSelected -> {
                    currentBookStatus = event.bookStatus
                }

                is BookDetailUiEvent.OnBookStatusUpdateButtonClick -> {
                    upsertBook(screen.bookId, currentBookStatus.value)
                }

                is BookDetailUiEvent.OnRecordSortBottomSheetDismiss -> {
                    isRecordSortBottomSheetVisible = false
                }

                is BookDetailUiEvent.OnRecordSortItemSelected -> {
                    currentRecordSort = event.sortType
                    isRecordSortBottomSheetVisible = false
                }

                is BookDetailUiEvent.OnRecordItemClick -> {
                    navigator.goTo(RecordDetailScreen(event.recordId))
                }
            }
        }

        return BookDetailUiState(
            isLoading = isLoading,
            bookDetail = bookDetail,
            isBookUpdateBottomSheetVisible = isBookUpdateBottomSheetVisible,
            isRecordSortBottomSheetVisible = isRecordSortBottomSheetVisible,
            currentBookStatus = currentBookStatus,
            currentRecordSort = currentRecordSort,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(BookDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: BookDetailScreen,
            navigator: Navigator,
        ): BookDetailPresenter
    }
}
