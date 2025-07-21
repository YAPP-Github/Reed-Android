package com.ninecraft.booket.feature.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.record.step.*
import com.ninecraft.booket.feature.screens.RecordSharedScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(RecordSharedScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordShared(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    // 시스템 백버튼 처리
    val isFirstStep = state.currentStep == RecordStep.BOOK_SELECT

    BackHandler(enabled = !isFirstStep) {
        // 첫 번째 스텝이 아닐 때만 이전 스텝으로 이동
        state.eventSink(RecordSharedUiEvent.PreviousStep)
    }

    ReedFullScreen(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        // Progress Indicator
        RecordProgressIndicator(
            currentStep = state.currentStep,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Step Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (state.currentStep) {
                RecordStep.BOOK_SELECT -> {
                    BookSelectStep(
                        state = state,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                RecordStep.READING_INFO -> {
                    ReadingInfoStep(
                        state = state,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                RecordStep.RECORD_WRITE -> {
                    RecordWriteStep(
                        state = state,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                RecordStep.QUOTATION -> {
                    QuotationStep(
                        state = state,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                RecordStep.REVIEW -> {
                    ReviewStep(
                        state = state,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                RecordStep.COMPLETE -> {
                    CompleteStep(
                        state = state,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Loading Overlay
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Buttons
        RecordNavigationButtons(
            state = state,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RecordProgressIndicator(
    currentStep: RecordStep,
    modifier: Modifier = Modifier,
) {
    val steps = RecordStep.entries
    val currentIndex = steps.indexOf(currentStep)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isCompleted = index < currentIndex
            val isCurrent = index == currentIndex

            Box(
                modifier = Modifier
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = when {
                        isCompleted -> MaterialTheme.colorScheme.primary
                        isCurrent -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.outline
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = when {
                                isCompleted -> MaterialTheme.colorScheme.onPrimary
                                isCurrent -> MaterialTheme.colorScheme.onPrimaryContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            }

            if (index < steps.size - 1) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = if (isCompleted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
        }
    }
}

@Composable
private fun RecordNavigationButtons(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Previous Button
        if (state.currentStep != RecordStep.BOOK_SELECT) {
            OutlinedButton(
                onClick = { state.eventSink(RecordSharedUiEvent.PreviousStep) },
                modifier = Modifier.weight(1f)
            ) {
                Text("이전")
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        // Next/Complete Button
        Button(
            onClick = {
                when (state.currentStep) {
                    RecordStep.REVIEW -> state.eventSink(RecordSharedUiEvent.SaveRecord)
                    RecordStep.COMPLETE -> state.eventSink(RecordSharedUiEvent.NavigateToHome)
                    else -> state.eventSink(RecordSharedUiEvent.NextStep)
                }
            },
            modifier = Modifier.weight(1f),
            enabled = !state.isLoading && isNextStepEnabled(state)
        ) {
            Text(
                text = when (state.currentStep) {
                    RecordStep.REVIEW -> "저장"
                    RecordStep.COMPLETE -> "완료"
                    else -> "다음"
                }
            )
        }
    }
}

private fun isNextStepEnabled(state: RecordSharedUiState): Boolean {
    return when (state.currentStep) {
        RecordStep.BOOK_SELECT -> state.recordData.selectedBookIsbn != null
        RecordStep.READING_INFO -> state.recordData.readingStatus != null
        RecordStep.RECORD_WRITE -> state.recordData.recordContent.isNotBlank()
        RecordStep.QUOTATION -> true // 인용구는 선택사항
        RecordStep.REVIEW -> true
        RecordStep.COMPLETE -> true
    }
}
