package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.feature.record.RecordSharedUiEvent
import com.ninecraft.booket.feature.record.RecordSharedUiState

@Composable
internal fun ReadingInfoStep(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "독서 정보를 입력해주세요",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Reading Status Selection
        ReadingStatusSection(
            selectedStatus = state.recordData.readingStatus,
            onStatusChange = { status ->
                state.eventSink(
                    RecordSharedUiEvent.UpdateReadingInfo(
                        status = status,
                        startDate = state.recordData.readingStartDate,
                        endDate = state.recordData.readingEndDate
                    )
                )
            }
        )

        // Date Selection
        DateSelectionSection(
            startDate = state.recordData.readingStartDate,
            endDate = state.recordData.readingEndDate,
            readingStatus = state.recordData.readingStatus,
            onDatesChange = { startDate, endDate ->
                state.eventSink(
                    RecordSharedUiEvent.UpdateReadingInfo(
                        status = state.recordData.readingStatus ?: "",
                        startDate = startDate,
                        endDate = endDate
                    )
                )
            }
        )
    }
}

@Composable
private fun ReadingStatusSection(
    selectedStatus: String?,
    onStatusChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "독서 상태",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        val statuses = listOf(
            "BEFORE_READING" to "읽기 전",
            "READING" to "읽는 중",
            "COMPLETED" to "완독",
            "STOPPED" to "중단"
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            statuses.forEach { (status, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedStatus == status,
                            onClick = { onStatusChange(status) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadioButton(
                        selected = selectedStatus == status,
                        onClick = { onStatusChange(status) }
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSelectionSection(
    startDate: String?,
    endDate: String?,
    readingStatus: String?,
    onDatesChange: (String?, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "독서 기간",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        // Start Date
        if (readingStatus != "BEFORE_READING") {
            var startDateInput by remember(startDate) { mutableStateOf(startDate ?: "") }

            OutlinedTextField(
                value = startDateInput,
                onValueChange = {
                    startDateInput = it
                    onDatesChange(it.takeIf { it.isNotBlank() }, endDate)
                },
                label = { Text("시작일") },
                placeholder = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // End Date
        if (readingStatus == "COMPLETED") {
            var endDateInput by remember(endDate) { mutableStateOf(endDate ?: "") }

            OutlinedTextField(
                value = endDateInput,
                onValueChange = {
                    endDateInput = it
                    onDatesChange(startDate, it.takeIf { it.isNotBlank() })
                },
                label = { Text("완료일") },
                placeholder = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // Helper text
        Text(
            text = when (readingStatus) {
                "BEFORE_READING" -> "읽기 전 상태에서는 날짜를 입력하지 않습니다."
                "READING" -> "읽기 시작한 날짜를 입력해주세요."
                "COMPLETED" -> "읽기 시작한 날짜와 완료한 날짜를 입력해주세요."
                "STOPPED" -> "읽기 시작한 날짜를 입력해주세요."
                else -> "독서 상태를 먼저 선택해주세요."
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
