package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.feature.record.Quotation
import com.ninecraft.booket.feature.record.RecordSharedUiState

@Composable
internal fun ReviewStep(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "작성한 독서 기록을 확인해주세요",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Book Information
        BookInfoSection(state = state)

        // Reading Information
        ReadingInfoSection(state = state)

        // Record Content
        RecordContentSection(state = state)

        // Quotations
        if (state.recordData.quotations.isNotEmpty()) {
            QuotationsSection(state = state)
        }

        // Tags
        if (state.recordData.tags.isNotEmpty()) {
            TagsSection(state = state)
        }

        // Confirmation message
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "저장 안내",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "저장 버튼을 누르면 독서 기록이 저장됩니다. 저장 후에도 수정할 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun BookInfoSection(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    ReviewSectionCard(
        title = "선택한 도서",
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = state.recordData.bookTitle ?: "제목 없음",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "저자: ${state.recordData.bookAuthor ?: "저자 정보 없음"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "ISBN: ${state.recordData.selectedBookIsbn ?: "ISBN 없음"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReadingInfoSection(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    ReviewSectionCard(
        title = "독서 정보",
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "상태: ${getReadingStatusLabel(state.recordData.readingStatus)}",
                style = MaterialTheme.typography.bodyMedium
            )

            state.recordData.readingStartDate?.let { startDate ->
                Text(
                    text = "시작일: $startDate",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.recordData.readingEndDate?.let { endDate ->
                Text(
                    text = "완료일: $endDate",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.recordData.rating?.let { rating ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "평점:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "★".repeat(rating) + "☆".repeat(5 - rating),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$rating/5",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordContentSection(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    ReviewSectionCard(
        title = "독서 기록",
        modifier = modifier
    ) {
        Text(
            text = state.recordData.recordContent.ifBlank { "작성된 기록이 없습니다." },
            style = MaterialTheme.typography.bodyMedium,
            color = if (state.recordData.recordContent.isBlank()) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
private fun QuotationsSection(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    ReviewSectionCard(
        title = "인용구 (${state.recordData.quotations.size}개)",
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.recordData.quotations.forEach { quotation ->
                QuotationReviewItem(quotation = quotation)
            }
        }
    }
}

@Composable
private fun QuotationReviewItem(
    quotation: Quotation,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = quotation.content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                quotation.pageNumber?.let { page ->
                    Text(
                        text = "p.$page",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (quotation.note.isNotBlank()) {
                Text(
                    text = quotation.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TagsSection(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    ReviewSectionCard(
        title = "태그",
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
        ) {
            state.recordData.tags.forEach { tag ->
                AssistChip(
                    onClick = { },
                    label = { Text(tag) },
                    enabled = false
                )
            }
        }
    }
}

@Composable
private fun ReviewSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            content()
        }
    }
}

private fun getReadingStatusLabel(status: String?): String {
    return when (status) {
        "BEFORE_READING" -> "읽기 전"
        "READING" -> "읽는 중"
        "COMPLETED" -> "완독"
        "STOPPED" -> "중단"
        else -> "알 수 없음"
    }
}
