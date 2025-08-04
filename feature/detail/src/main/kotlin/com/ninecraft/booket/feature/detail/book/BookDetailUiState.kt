package com.ninecraft.booket.feature.detail.book

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.common.R
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.model.RecordRegisterModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class BookDetailUiState(
    val isLoading: Boolean = false,
    val bookDetail: BookDetailModel = BookDetailModel(),
    val seedsStats: ImmutableList<EmotionModel> = persistentListOf(),
    val isBookUpdateBottomSheetVisible: Boolean = false,
    val isRecordSortBottomSheetVisible: Boolean = false,
    val currentBookStatus: BookStatus = BookStatus.BEFORE_READING,
    val currentRecordSort: RecordSort = RecordSort.PAGE_ASCENDING,
    val recordCollections: ImmutableList<RecordRegisterModel> = persistentListOf(
        RecordRegisterModel(
            id = "0",
            pageNumber = 12,
            quote = "“책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.“",
            createdAt = "2025.06.25",
        ),
        RecordRegisterModel(
            id = "1",
            pageNumber = 13,
            quote = "“책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.“",
            createdAt = "2025.06.25",
        ),
        RecordRegisterModel(
            id = "2",
            pageNumber = 14,
            quote = "“책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.“",
            createdAt = "2025.06.25",
        ),
        RecordRegisterModel(
            id = "3",
            pageNumber = 15,
            quote = "“책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.책을 읽으면 차분해지며 숲으로 둘러싸인 여름 별장 속으로 간 것 같은 기분이 든다. 그 곳에서 그들이 품은 건축에 대한 이상과 삶을 구경하는 것만으로도 충분했다.“",
            createdAt = "2025.06.25",
        ),
    ),
    val sideEffect: BookDetailSideEffect? = null,
    val eventSink: (BookDetailUiEvent) -> Unit,
) : CircuitUiState

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
    data class OnRecordItemClick(val recordId: String) : BookDetailUiEvent
}

enum class RecordSort(val value: String) {
    PAGE_ASCENDING("PAGE_ASCENDING"),
    RECENT_REGISTER("RECENT_REGISTER"),
    ;

    fun getDisplayNameRes(): Int {
        return when (this) {
            PAGE_ASCENDING -> R.string.record_sort_page_ascending
            RECENT_REGISTER -> R.string.record_sort_recent_register
        }
    }

    companion object Companion {
        fun fromValue(value: String): RecordSort? {
            return entries.find { it.value == value }
        }
    }
}
