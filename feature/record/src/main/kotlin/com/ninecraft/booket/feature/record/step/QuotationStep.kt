package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.feature.record.Quotation
import com.ninecraft.booket.feature.record.RecordSharedUiEvent
import com.ninecraft.booket.feature.record.RecordSharedUiState

@Composable
internal fun QuotationStep(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "인상 깊었던 구절을 추가해보세요",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "이 단계는 선택사항입니다. 건너뛰어도 됩니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Add new quotation form
        AddQuotationForm(
            onAddQuotation = { quotation ->
                state.eventSink(RecordSharedUiEvent.AddQuotation(quotation))
            }
        )

        // List of quotations
        if (state.recordData.quotations.isNotEmpty()) {
            Text(
                text = "추가된 인용구 (${state.recordData.quotations.size}개)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.recordData.quotations,
                    key = { it.id }
                ) { quotation ->
                    QuotationCard(
                        quotation = quotation,
                        onEdit = { updatedQuotation ->
                            state.eventSink(RecordSharedUiEvent.UpdateQuotation(updatedQuotation))
                        },
                        onDelete = {
                            state.eventSink(RecordSharedUiEvent.RemoveQuotation(quotation.id))
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "아직 추가된 인용구가 없습니다",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AddQuotationForm(
    onAddQuotation: (Quotation) -> Unit,
    modifier: Modifier = Modifier,
) {
    var content by remember { mutableStateOf("") }
    var pageNumber by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "새 인용구 추가",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    if (!isExpanded && it.isNotBlank()) {
                        isExpanded = true
                    }
                },
                label = { Text("인용구") },
                placeholder = { Text("기억하고 싶은 구절을 입력해주세요") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 5
            )

            if (isExpanded) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = pageNumber,
                        onValueChange = { pageNumber = it },
                        label = { Text("페이지") },
                        placeholder = { Text("123") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("메모 (선택사항)") },
                    placeholder = { Text("이 구절에 대한 생각이나 느낀 점") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                if (isExpanded && (content.isNotBlank() || pageNumber.isNotBlank() || note.isNotBlank())) {
                    TextButton(
                        onClick = {
                            content = ""
                            pageNumber = ""
                            note = ""
                            isExpanded = false
                        }
                    ) {
                        Text("취소")
                    }
                }

                Button(
                    onClick = {
                        if (content.isNotBlank()) {
                            onAddQuotation(
                                Quotation(
                                    id = "",
                                    content = content.trim(),
                                    pageNumber = pageNumber.toIntOrNull(),
                                    note = note.trim()
                                )
                            )
                            content = ""
                            pageNumber = ""
                            note = ""
                            isExpanded = false
                        }
                    },
                    enabled = content.isNotBlank()
                ) {
                    Text("추가")
                }
            }
        }
    }
}

@Composable
private fun QuotationCard(
    quotation: Quotation,
    onEdit: (Quotation) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isEditing by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isEditing) {
                EditQuotationContent(
                    quotation = quotation,
                    onSave = { updatedQuotation ->
                        onEdit(updatedQuotation)
                        isEditing = false
                    },
                    onCancel = { isEditing = false }
                )
            } else {
                DisplayQuotationContent(
                    quotation = quotation,
                    onEdit = { isEditing = true },
                    onDelete = onDelete
                )
            }
        }
    }
}

@Composable
private fun DisplayQuotationContent(
    quotation: Quotation,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = quotation.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
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
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            TextButton(onClick = onEdit) {
                Text("수정")
            }
            TextButton(onClick = onDelete) {
                Text("삭제")
            }
        }
    }
}

@Composable
private fun EditQuotationContent(
    quotation: Quotation,
    onSave: (Quotation) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var content by remember { mutableStateOf(quotation.content) }
    var pageNumber by remember { mutableStateOf(quotation.pageNumber?.toString() ?: "") }
    var note by remember { mutableStateOf(quotation.note) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("인용구") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 5
        )

        OutlinedTextField(
            value = pageNumber,
            onValueChange = { pageNumber = it },
            label = { Text("페이지") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("메모") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            TextButton(onClick = onCancel) {
                Text("취소")
            }
            Button(
                onClick = {
                    onSave(
                        quotation.copy(
                            content = content.trim(),
                            pageNumber = pageNumber.toIntOrNull(),
                            note = note.trim()
                        )
                    )
                },
                enabled = content.isNotBlank()
            ) {
                Text("저장")
            }
        }
    }
}
