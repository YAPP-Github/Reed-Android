package com.ninecraft.booket.feature.detail.book

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.model.ReadingRecordModel
import com.ninecraft.booket.core.ui.component.FooterState
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class BookDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: BookDetailScreen,
    @Assisted private val navigator: Navigator,
    private val bookRepository: BookRepository,
    private val recordRepository: RecordRepository,
) : Presenter<BookDetailUiState> {
    companion object {
        private const val PAGE_SIZE = 20
        private const val START_INDEX = 1
    }

    @Composable
    override fun present(): BookDetailUiState {
        val scope = rememberCoroutineScope()
        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var footerState by rememberRetained { mutableStateOf<FooterState>(FooterState.Idle) }
        var bookDetail by rememberRetained { mutableStateOf(BookDetailModel()) }
        var seedsStates by rememberRetained { mutableStateOf<ImmutableList<EmotionModel>>(persistentListOf()) }
        var readingRecords by rememberRetained { mutableStateOf(persistentListOf<ReadingRecordModel>()) }
        var currentStartIndex by rememberRetained { mutableIntStateOf(START_INDEX) }
        var isLastPage by rememberRetained { mutableStateOf(false) }
        var currentBookStatus by rememberRetained { mutableStateOf(BookStatus.READING) }
        var currentRecordSort by rememberRetained { mutableStateOf(RecordSort.PAGE_NUMBER_ASC) }
        var isBookUpdateBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isRecordSortBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<BookDetailSideEffect?>(null) }

        fun getSeedsStats() {
            scope.launch {
                bookRepository.getSeedsStats(screen.userBookId)
                    .onSuccess { result ->
                        seedsStates = result.categories.toImmutableList()
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

        fun getBookDetail() {
            scope.launch {
                bookRepository.getBookDetail(screen.isbn)
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
            }
        }

        fun upsertBook(bookIsbn: String, bookStatus: String) {
            scope.launch {
                bookRepository.upsertBook(bookIsbn, bookStatus)
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

        fun getReadingRecords(startIndex: Int = START_INDEX) {
            scope.launch {
                if (startIndex == START_INDEX) {
                    uiState = UiState.Loading
                } else {
                    footerState = FooterState.Loading
                }

                recordRepository.getReadingRecords(
                    userBookId = screen.userBookId,
                    sort = currentRecordSort.value,
                    page = START_INDEX,
                    size = PAGE_SIZE,
                ).onSuccess { result ->
                    readingRecords = if (startIndex == START_INDEX) {
                        result.content.toPersistentList()
                    } else {
                        (readingRecords + result.content).toPersistentList()
                    }

                    currentStartIndex = startIndex
                    isLastPage = result.content.size < PAGE_SIZE

                    if (startIndex == START_INDEX) {
                        uiState = UiState.Success
                    } else {
                        footerState = if (isLastPage) FooterState.End else FooterState.Idle
                    }
                }.onFailure { exception ->
                    Logger.d(exception)
                    val errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다."
                    if (startIndex == START_INDEX) {
                        uiState = UiState.Error(errorMessage)
                    } else {
                        footerState = FooterState.Error(errorMessage)
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            getSeedsStats()
            getBookDetail()
            getReadingRecords()
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
                    upsertBook(screen.isbn13, currentBookStatus.value)
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

                is BookDetailUiEvent.OnLoadMore -> {
                    if (footerState !is FooterState.Loading && !isLastPage) {
                        getReadingRecords(startIndex = currentStartIndex + 1)
                    }
                }
            }
        }

        return BookDetailUiState(
            uiState = uiState,
            footerState = footerState,
            bookDetail = bookDetail,
            seedsStats = seedsStates,
            readingRecords = readingRecords,
            currentStartIndex = currentStartIndex,
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
