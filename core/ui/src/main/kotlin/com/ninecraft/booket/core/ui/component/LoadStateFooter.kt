package com.ninecraft.booket.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.R

@Composable
fun LoadStateFooter(
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
                    color = ReedTheme.colors.contentBrand,
                )
            }

            is FooterState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
                ) {
                    Text(
                        text = footerState.message,
                        color = ReedTheme.colors.contentError,
                        style = ReedTheme.typography.body2Regular,
                    )
                    Button(onClick = onRetryClick) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            is FooterState.End -> {
                Text(
                    text = stringResource(R.string.no_more_result),
                    color = ReedTheme.colors.contentSecondary,
                    style = ReedTheme.typography.body2Regular,
                )
            }

            is FooterState.Idle -> {
                // No content
            }
        }
    }
}

sealed interface FooterState {
    data object Idle : FooterState
    data object Loading : FooterState
    data object End : FooterState
    data class Error(val message: String) : FooterState
}
