package com.ninecraft.booket.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.search.FooterState
import com.ninecraft.booket.feature.search.R

@Composable
internal fun LoadStateFooter(
    footerState: FooterState,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(ReedTheme.spacing.spacing4),
        contentAlignment = Alignment.Center,
    ) {
        when (footerState) {
            is FooterState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            is FooterState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
                ) {
                    Text(
                        text = footerState.message,
                        style = ReedTheme.typography.body2Regular,
                        color = MaterialTheme.colorScheme.error,
                    )
                    Button(onClick = onRetryClick) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            is FooterState.End -> {
                Text(
                    text = stringResource(R.string.no_more_results),
                    style = ReedTheme.typography.body2Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            is FooterState.Idle -> {
                // No content
            }
        }
    }
}