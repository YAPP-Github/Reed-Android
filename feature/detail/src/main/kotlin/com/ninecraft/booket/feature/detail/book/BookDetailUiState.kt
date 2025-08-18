package com.ninecraft.booket.feature.detail.book

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.common.R
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.model.ReadingRecordModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data class Error(val exception: Throwable) : UiState
}

data class BookDetailUiState(
    val uiState: UiState = UiState.Idle,
    val footerState: FooterState = FooterState.Idle,
    val isLoading: Boolean = false,
    val bookDetail: BookDetailModel = BookDetailModel(),
    val seedsStats: ImmutableList<EmotionModel> = persistentListOf(),
    val readingRecords: ImmutableList<ReadingRecordModel> = persistentListOf(),
    val readingRecordsTotalCount: Int = 0,
    val currentStartIndex: Int = 1,
    val isLastPage: Boolean = false,
    val currentBookStatus: BookStatus = BookStatus.BEFORE_READING,
    val selectedBookStatus: BookStatus = BookStatus.BEFORE_READING,
    val currentRecordSort: RecordSort = RecordSort.PAGE_NUMBER_ASC,
    val selectedRecordInfo: ReadingRecordModel = ReadingRecordModel(),
    val isBookUpdateBottomSheetVisible: Boolean = false,
    val isRecordSortBottomSheetVisible: Boolean = false,
    val isRecordMenuBottomSheetVisible: Boolean = false,
    val isRecordDeleteDialogVisible: Boolean = false,
    val sideEffect: BookDetailSideEffect? = null,
    val eventSink: (BookDetailUiEvent) -> Unit,
) : CircuitUiState {

    fun hasEmotionData(): Boolean {
        return seedsStats.any { it.count > 0 }
    }
}

@Immutable
sealed interface BookDetailSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : BookDetailSideEffect
}

sealed interface BookDetailUiEvent : CircuitUiEvent {
    data object InitSideEffect : BookDetailUiEvent
    data object OnBackClick : BookDetailUiEvent
    data object OnBookStatusButtonClick : BookDetailUiEvent
    data object OnRegisterRecordButtonClick : BookDetailUiEvent
    data object OnRecordSortButtonClick : BookDetailUiEvent
    data object OnBookUpdateBottomSheetDismiss : BookDetailUiEvent
    data class OnBookStatusItemSelected(val bookStatus: BookStatus) : BookDetailUiEvent
    data object OnBookStatusUpdateButtonClick : BookDetailUiEvent
    data object OnRecordSortBottomSheetDismiss : BookDetailUiEvent
    data class OnRecordSortItemSelected(val sortType: RecordSort) : BookDetailUiEvent
    data class OnRecordMenuClick(val selectedRecordInfo: ReadingRecordModel) : BookDetailUiEvent
    data object OnRecordMenuBottomSheetDismiss : BookDetailUiEvent
    data object OnRecordDeleteDialogDismiss : BookDetailUiEvent
    data object OnEditRecordClick : BookDetailUiEvent
    data object OnDeleteRecordClick : BookDetailUiEvent
    data object OnDelete : BookDetailUiEvent
    data class OnRecordItemClick(val recordId: String) : BookDetailUiEvent
    data object OnLoadMore : BookDetailUiEvent
    data object OnRetryClick : BookDetailUiEvent
}

enum class RecordSort(val value: String) {
    PAGE_NUMBER_ASC("PAGE_NUMBER_ASC"),
    CREATED_DATE_DESC("CREATED_DATE_DESC"),
    ;

    fun getDisplayNameRes(): Int {
        return when (this) {
            PAGE_NUMBER_ASC -> R.string.record_sort_page_ascending
            CREATED_DATE_DESC -> R.string.record_sort_recent_register
        }
    }

    companion object Companion {
        fun fromValue(value: String): RecordSort? {
            return entries.find { it.value == value }
        }
    }
}
