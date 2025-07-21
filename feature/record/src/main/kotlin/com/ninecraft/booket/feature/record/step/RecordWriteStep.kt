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
import com.ninecraft.booket.feature.record.RecordSharedUiEvent
import com.ninecraft.booket.feature.record.RecordSharedUiState

@Composable
internal fun RecordWriteStep(
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
            text = "독서 기록을 작성해주세요",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Rating Section
        RatingSection(
            rating = state.recordData.rating,
            onRatingChange = { rating ->
                state.eventSink(RecordSharedUiEvent.UpdateRating(rating))
            }
        )

        // Record Content
        RecordContentSection(
            content = state.recordData.recordContent,
            onContentChange = { content ->
                state.eventSink(RecordSharedUiEvent.UpdateRecord(content))
            }
        )

        // Tags Section
        TagsSection(
            tags = state.recordData.tags,
            onAddTag = { tag ->
                state.eventSink(RecordSharedUiEvent.AddTag(tag))
            },
            onRemoveTag = { tag ->
                state.eventSink(RecordSharedUiEvent.RemoveTag(tag))
            }
        )
    }
}

@Composable
private fun RatingSection(
    rating: Int?,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "평점",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { star ->
                val isSelected = rating != null && star <= rating

                TextButton(
                    onClick = { onRatingChange(star) },
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text(
                        text = if (isSelected) "★" else "☆",
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        }
                    )
                }
            }

            if (rating != null) {
                Text(
                    text = "$rating/5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RecordContentSection(
    content: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "독서 기록",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = { Text("기록 내용") },
            placeholder = {
                Text("이 책에 대한 생각, 느낀 점, 인상 깊었던 부분 등을 자유롭게 작성해보세요.")
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            minLines = 5,
            maxLines = 10
        )

        Text(
            text = "${content.length} 자",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun TagsSection(
    tags: List<String>,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "태그",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        var newTag by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTag,
                onValueChange = { newTag = it },
                label = { Text("태그 추가") },
                placeholder = { Text("예: 자기계발, 소설, 추천") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Button(
                onClick = {
                    if (newTag.isNotBlank()) {
                        onAddTag(newTag.trim())
                        newTag = ""
                    }
                },
                enabled = newTag.isNotBlank()
            ) {
                Text("추가")
            }
        }

        // Display current tags
        if (tags.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
            ) {
                tags.forEach { tag ->
                    AssistChip(
                        onClick = { onRemoveTag(tag) },
                        label = { Text(tag) },
                        trailingIcon = {
                            Text(
                                text = "×",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    )
                }
            }
        }

        Text(
            text = "태그를 클릭하면 삭제됩니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
