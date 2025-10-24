package com.ninecraft.booket.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.mediumButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.R

@Immutable
sealed interface ErrorType {
    data object NetworkError : ErrorType
    data object ServerError : ErrorType
}

@Composable
fun ReedErrorUi(
    errorType: ErrorType,
    onRetryClick: () -> Unit,
) {
    val message = when (errorType) {
        ErrorType.NetworkError -> stringResource(R.string.network_error_message)
        ErrorType.ServerError -> stringResource(R.string.server_error_message)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                color = ReedTheme.colors.contentSecondary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
            ReedButton(
                onClick = { onRetryClick() },
                text = stringResource(R.string.retry),
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = mediumButtonStyle,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ReedNetworkErrorUiPreview() {
    ReedTheme {
        ReedErrorUi(
            errorType = ErrorType.NetworkError,
            onRetryClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun ReedServerErrorUiPreview() {
    ReedTheme {
        ReedErrorUi(
            errorType = ErrorType.ServerError,
            onRetryClick = {},
        )
    }
}
