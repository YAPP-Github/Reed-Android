package com.ninecraft.booket.feature.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.RecordSharedScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import java.util.UUID

class RecordSharedPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RecordSharedUiState> {

    @Composable
    override fun present(): RecordSharedUiState {
        var currentStep by rememberRetained { mutableStateOf(RecordStep.BOOK_SELECT) }
        var recordData by rememberRetained { mutableStateOf(RecordData()) }
        var isLoading by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: RecordSharedUiEvent) {
            when (event) {
                is RecordSharedUiEvent.NavigateToStep -> {
                    currentStep = event.step
                }

                is RecordSharedUiEvent.SelectBook -> {
                    recordData = recordData.copy(
                        selectedBookIsbn = event.isbn,
                        bookTitle = event.title,
                        bookAuthor = event.author,
                        bookImageUrl = event.imageUrl,
                    )
                }

                is RecordSharedUiEvent.UpdateReadingInfo -> {
                    recordData = recordData.copy(
                        readingStatus = event.status,
                        readingStartDate = event.startDate,
                        readingEndDate = event.endDate,
                    )
                }

                is RecordSharedUiEvent.UpdateRecord -> {
                    recordData = recordData.copy(recordContent = event.content)
                }

                is RecordSharedUiEvent.UpdateRating -> {
                    recordData = recordData.copy(rating = event.rating)
                }

                is RecordSharedUiEvent.AddQuotation -> {
                    val newQuotation = event.quotation.copy(
                        id = event.quotation.id.ifEmpty { UUID.randomUUID().toString() }
                    )
                    recordData = recordData.copy(
                        quotations = recordData.quotations + newQuotation
                    )
                }

                is RecordSharedUiEvent.RemoveQuotation -> {
                    recordData = recordData.copy(
                        quotations = recordData.quotations.filterNot { it.id == event.quotationId }
                    )
                }

                is RecordSharedUiEvent.UpdateQuotation -> {
                    recordData = recordData.copy(
                        quotations = recordData.quotations.map { quotation ->
                            if (quotation.id == event.quotation.id) event.quotation else quotation
                        }
                    )
                }

                is RecordSharedUiEvent.AddTag -> {
                    if (event.tag.isNotBlank() && !recordData.tags.contains(event.tag)) {
                        recordData = recordData.copy(tags = recordData.tags + event.tag)
                    }
                }

                is RecordSharedUiEvent.RemoveTag -> {
                    recordData = recordData.copy(tags = recordData.tags - event.tag)
                }

                RecordSharedUiEvent.NextStep -> {
                    val nextStep = when (currentStep) {
                        RecordStep.BOOK_SELECT -> RecordStep.READING_INFO
                        RecordStep.READING_INFO -> RecordStep.RECORD_WRITE
                        RecordStep.RECORD_WRITE -> RecordStep.QUOTATION
                        RecordStep.QUOTATION -> RecordStep.REVIEW
                        RecordStep.REVIEW -> RecordStep.COMPLETE
                        RecordStep.COMPLETE -> RecordStep.COMPLETE
                    }
                    currentStep = nextStep
                }

                RecordSharedUiEvent.PreviousStep -> {
                    val previousStep = when (currentStep) {
                        RecordStep.BOOK_SELECT -> RecordStep.BOOK_SELECT
                        RecordStep.READING_INFO -> RecordStep.BOOK_SELECT
                        RecordStep.RECORD_WRITE -> RecordStep.READING_INFO
                        RecordStep.QUOTATION -> RecordStep.RECORD_WRITE
                        RecordStep.REVIEW -> RecordStep.QUOTATION
                        RecordStep.COMPLETE -> RecordStep.REVIEW
                    }
                    currentStep = previousStep
                }

                RecordSharedUiEvent.SaveRecord -> {
                    isLoading = true
                    // TODO: Implement actual save logic with repository
                    // For now, just simulate completion
                    currentStep = RecordStep.COMPLETE
                    isLoading = false
                }

                RecordSharedUiEvent.OnBackPressed -> {
                    if (currentStep == RecordStep.BOOK_SELECT) {
                        navigator.pop()
                    } else {
                        handleEvent(RecordSharedUiEvent.PreviousStep)
                    }
                }

                RecordSharedUiEvent.NavigateToHome -> {
                    navigator.pop()
                }
            }
        }

        return RecordSharedUiState(
            currentStep = currentStep,
            recordData = recordData,
            isLoading = isLoading,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(RecordSharedScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): RecordSharedPresenter
    }
}
