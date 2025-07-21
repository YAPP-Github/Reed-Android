package com.ninecraft.booket.feature.record

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class RecordSharedUiState(
    val currentStep: RecordStep = RecordStep.BOOK_SELECT,
    val recordData: RecordData = RecordData(),
    val isLoading: Boolean = false,
    val eventSink: (RecordSharedUiEvent) -> Unit,
) : CircuitUiState

@Immutable
data class RecordData(
    val selectedBookIsbn: String? = null,
    val bookTitle: String? = null,
    val bookAuthor: String? = null,
    val bookImageUrl: String? = null,
    val readingStatus: String? = null,
    val readingStartDate: String? = null,
    val readingEndDate: String? = null,
    val rating: Int? = null,
    val recordContent: String = "",
    val quotations: List<Quotation> = emptyList(),
    val tags: List<String> = emptyList(),
    val sampleBooks: List<SampleBook> = getSampleBooks(),
)

@Immutable
data class SampleBook(
    val isbn: String,
    val title: String,
    val author: String,
    val imageUrl: String? = null,
)

private fun getSampleBooks(): List<SampleBook> = listOf(
    SampleBook(
        isbn = "9788932473901",
        title = "클린 코드",
        author = "로버트 C. 마틴"
    ),
    SampleBook(
        isbn = "9788966262281",
        title = "이펙티브 자바",
        author = "조슈아 블로크"
    ),
    SampleBook(
        isbn = "9791162243930",
        title = "코틀린 인 액션",
        author = "드미트리 제메로프, 스베트라나 이사코바"
    ),
    SampleBook(
        isbn = "9788968481475",
        title = "Clean Architecture",
        author = "로버트 C. 마틴"
    ),
    SampleBook(
        isbn = "9791158391409",
        title = "리팩터링 2판",
        author = "마틴 파울러"
    ),
    SampleBook(
        isbn = "9788966262984",
        title = "디자인 패턴",
        author = "Gang of Four"
    ),
    SampleBook(
        isbn = "9791189909048",
        title = "혼자 공부하는 자바",
        author = "신용권"
    ),
    SampleBook(
        isbn = "9788979149548",
        title = "알고리즘 문제 해결 전략",
        author = "구종만"
    )
)

@Immutable
data class Quotation(
    val id: String,
    val content: String,
    val pageNumber: Int? = null,
    val note: String = "",
)

enum class RecordStep {
    BOOK_SELECT,        // 도서 선택
    READING_INFO,       // 독서 정보 입력 (시작일, 종료일, 상태)
    RECORD_WRITE,       // 독서 기록 작성
    QUOTATION,          // 인용구 추가
    REVIEW,             // 최종 검토
    COMPLETE            // 완료
}

sealed interface RecordSharedUiEvent : CircuitUiEvent {
    data object OnBackPressed : RecordSharedUiEvent
    data class NavigateToStep(val step: RecordStep) : RecordSharedUiEvent
    data class SelectBook(
        val isbn: String,
        val title: String,
        val author: String,
        val imageUrl: String?,
    ) : RecordSharedUiEvent
    data class UpdateReadingInfo(
        val status: String,
        val startDate: String?,
        val endDate: String?,
    ) : RecordSharedUiEvent
    data class UpdateRecord(val content: String) : RecordSharedUiEvent
    data class UpdateRating(val rating: Int) : RecordSharedUiEvent
    data class AddQuotation(val quotation: Quotation) : RecordSharedUiEvent
    data class RemoveQuotation(val quotationId: String) : RecordSharedUiEvent
    data class UpdateQuotation(val quotation: Quotation) : RecordSharedUiEvent
    data class AddTag(val tag: String) : RecordSharedUiEvent
    data class RemoveTag(val tag: String) : RecordSharedUiEvent
    data object NextStep : RecordSharedUiEvent
    data object PreviousStep : RecordSharedUiEvent
    data object SaveRecord : RecordSharedUiEvent
    data object NavigateToHome: RecordSharedUiEvent
}
