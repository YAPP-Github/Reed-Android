package com.ninecraft.booket.feature.detail.book

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.common.constants.ErrorScope
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.common.utils.postErrorDialog
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.model.PageInfoModel
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BookDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: BookDetailScreen,
    @Assisted private val navigator: Navigator,
    private val bookRepository: BookRepository,
    private val recordRepository: RecordRepository,
) : Presenter<BookDetailUiState> {
    companion object {
        private const val PAGE_SIZE = 20
        private const val START_INDEX = 0
    }

    private fun getRecordComparator(sortType: RecordSort): Comparator<ReadingRecordModel> {
        return when (sortType) {
            RecordSort.PAGE_NUMBER_ASC -> compareBy { it.pageNumber }
            RecordSort.CREATED_DATE_DESC -> compareByDescending { LocalDateTime.parse(it.createdAt) }
        }
    }

    @Composable
    override fun present(): BookDetailUiState {
        val scope = rememberCoroutineScope()
        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var footerState by rememberRetained { mutableStateOf<FooterState>(FooterState.Idle) }
        var bookDetail by rememberRetained { mutableStateOf(BookDetailModel()) }
        var seedsStates by rememberRetained { mutableStateOf<ImmutableList<EmotionModel>>(persistentListOf()) }
        var readingRecords by rememberRetained { mutableStateOf(persistentListOf<ReadingRecordModel>()) }
        var readingRecordsPageInfo by rememberRetained { mutableStateOf(PageInfoModel()) }
        var currentStartIndex by rememberRetained { mutableIntStateOf(START_INDEX) }
        var isLastPage by rememberRetained { mutableStateOf(false) }
        var currentBookStatus by rememberRetained { mutableStateOf(BookStatus.READING) }
        var selectedBookStatus by rememberRetained { mutableStateOf(BookStatus.READING) }
        var currentRecordSort by rememberRetained { mutableStateOf(RecordSort.PAGE_NUMBER_ASC) }
        var isBookUpdateBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isRecordSortBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<BookDetailSideEffect?>(null) }

        @Suppress("TooGenericExceptionCaught")
        fun initialLoad() {
            uiState = UiState.Loading

            try {
                scope.launch {
                    val bookDetailDef = async { bookRepository.getBookDetail(screen.isbn13).getOrThrow() }
                    val seedsDef = async { bookRepository.getSeedsStats(screen.userBookId).getOrThrow() }
                    val readingRecordsDef = async {
                        recordRepository.getReadingRecords(
                            userBookId = screen.userBookId,
                            sort = currentRecordSort.value,
                            page = START_INDEX,
                            size = PAGE_SIZE,
                        ).getOrThrow()
                    }
                    val detail = bookDetailDef.await()
                    val seeds = seedsDef.await()
                    val records = readingRecordsDef.await()

                    bookDetail = detail
                    currentBookStatus = BookStatus.fromValue(detail.userBookStatus) ?: BookStatus.BEFORE_READING
                    selectedBookStatus = currentBookStatus
                    seedsStates = seeds.categories.toImmutableList()
                    readingRecords = records.content.toPersistentList()
                    readingRecordsPageInfo = records.page

                    isLastPage = records.content.size < PAGE_SIZE
                    currentStartIndex = START_INDEX

                    uiState = UiState.Success
                }
            } catch (e: Throwable) {
                uiState = UiState.Error(e)

                val handleErrorMessage = { message: String ->
                    Logger.e(message)
                    sideEffect = BookDetailSideEffect.ShowToast(message)
                }

                handleException(
                    exception = e,
                    onError = handleErrorMessage,
                    onLoginRequired = {
                        navigator.resetRoot(LoginScreen)
                    },
                )
            }
        }

        fun upsertBook(isbn13: String, bookStatus: String) {
            scope.launch {
                bookRepository.upsertBook(isbn13, bookStatus)
                    .onSuccess {
                        currentBookStatus = BookStatus.fromValue(bookStatus) ?: BookStatus.BEFORE_READING
                        bookDetail = bookDetail.copy(userBookStatus = bookStatus)
                        isBookUpdateBottomSheetVisible = false
                    }
                    .onFailure { exception ->
                        postErrorDialog(
                            errorScope = ErrorScope.BOOK_REGISTER,
                            exception = exception,
                        )

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

        fun loadMoreReadingRecords(startIndex: Int) {
            // 초기 페이지 로드는 initialLoad()에서 담당하므로 무시
            if (startIndex == START_INDEX || isLastPage) {
                return
            }

            scope.launch {
                footerState = FooterState.Loading

                recordRepository.getReadingRecords(
                    userBookId = screen.userBookId,
                    sort = currentRecordSort.value,
                    page = startIndex,
                    size = PAGE_SIZE,
                ).onSuccess { result ->
                    readingRecords = (readingRecords + result.content).toPersistentList()
                    currentStartIndex = startIndex
                    isLastPage = result.content.size < PAGE_SIZE
                    footerState = if (isLastPage) FooterState.End else FooterState.Idle
                }.onFailure { exception ->
                    Logger.d(exception)
                    val errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다."
                    footerState = FooterState.Error(errorMessage)
                }
            }
        }

        LaunchedEffect(Unit) {
            initialLoad()
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
                    navigator.goTo(RecordScreen(screen.userBookId))
                }

                is BookDetailUiEvent.OnRecordSortButtonClick -> {
                    isRecordSortBottomSheetVisible = true
                }

                is BookDetailUiEvent.OnBookUpdateBottomSheetDismiss -> {
                    isBookUpdateBottomSheetVisible = false
                }

                is BookDetailUiEvent.OnBookStatusItemSelected -> {
                    selectedBookStatus = event.bookStatus
                }

                is BookDetailUiEvent.OnBookStatusUpdateButtonClick -> {
                    upsertBook(screen.isbn13, selectedBookStatus.value)
                }

                is BookDetailUiEvent.OnRecordSortBottomSheetDismiss -> {
                    isRecordSortBottomSheetVisible = false
                }

                is BookDetailUiEvent.OnRecordSortItemSelected -> {
                    currentRecordSort = event.sortType
                    readingRecords = readingRecords.sortedWith(getRecordComparator(event.sortType)).toPersistentList()
                    isRecordSortBottomSheetVisible = false
                }

                is BookDetailUiEvent.OnRecordItemClick -> {
                    navigator.goTo(RecordDetailScreen(event.recordId))
                }

                is BookDetailUiEvent.OnLoadMore -> {
                    if (uiState != UiState.Loading && footerState !is FooterState.Loading && !isLastPage) {
                        loadMoreReadingRecords(startIndex = currentStartIndex + 1)
                    }
                }

                is BookDetailUiEvent.OnRetryClick -> {
                    scope.launch {
                        initialLoad()
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
            readingRecordsPageInfo = readingRecordsPageInfo,
            isBookUpdateBottomSheetVisible = isBookUpdateBottomSheetVisible,
            isRecordSortBottomSheetVisible = isRecordSortBottomSheetVisible,
            currentBookStatus = currentBookStatus,
            selectedBookStatus = selectedBookStatus,
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
