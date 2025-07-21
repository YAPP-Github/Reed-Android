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
import com.ninecraft.booket.feature.record.RecordSharedUiEvent
import com.ninecraft.booket.feature.record.RecordSharedUiState
import com.ninecraft.booket.feature.record.SampleBook

@Composable
internal fun BookSelectStep(
    state: RecordSharedUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "독서 기록할 도서를 선택해주세요",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Search Field
        var searchQuery by remember { mutableStateOf("") }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("도서 검색") },
            placeholder = { Text("제목, 저자, ISBN으로 검색하세요") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Selected Book Display
        state.recordData.selectedBookIsbn?.let { isbn ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "선택된 도서",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.recordData.bookTitle ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = state.recordData.bookAuthor ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Temporary: Sample books for demonstration
        if (searchQuery.isNotEmpty() && state.recordData.selectedBookIsbn == null) {
            Text(
                text = "검색 결과",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.recordData.sampleBooks.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                    it.author.contains(searchQuery, ignoreCase = true)
                }) { book ->
                    BookSearchItem(
                        book = book,
                        onSelect = {
                            state.eventSink(
                                RecordSharedUiEvent.SelectBook(
                                    isbn = book.isbn,
                                    title = book.title,
                                    author = book.author,
                                    imageUrl = book.imageUrl
                                )
                            )
                        }
                    )
                }
            }
        }

        // Empty state
        if (state.recordData.selectedBookIsbn == null && searchQuery.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "검색어를 입력하여 도서를 찾아보세요",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BookSearchItem(
    book: SampleBook,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onSelect,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Placeholder for book cover
            Surface(
                modifier = Modifier.size(60.dp, 80.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "📖",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "ISBN: ${book.isbn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

