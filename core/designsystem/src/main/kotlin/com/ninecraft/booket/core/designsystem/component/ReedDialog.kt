package com.ninecraft.booket.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun ReedDialog(
    title: String,
    confirmButtonText: String,
    onConfirmRequest: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    dismissButtonText: String? = null,
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = ReedTheme.colors.basePrimary,
                    shape = RoundedCornerShape(
                        ReedTheme.radius.lg,
                    ),
                )
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing8,
                    end = ReedTheme.spacing.spacing5,
                    bottom = ReedTheme.spacing.spacing5,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                color = ReedTheme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.headline1SemiBold,
            )
            description?.let {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
                Text(
                    text = description,
                    color = ReedTheme.colors.contentSecondary,
                    textAlign = TextAlign.Center,
                    style = ReedTheme.typography.body2Medium,
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                dismissButtonText?.let {
                    ReedButton(
                        onClick = {
                            onDismissRequest()
                        },
                        sizeStyle = largeButtonStyle,
                        colorStyle = ReedButtonColorStyle.SECONDARY,
                        modifier = Modifier.weight(1f),
                        text = dismissButtonText,
                    )
                    Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                }
                ReedButton(
                    onClick = {
                        onConfirmRequest()
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    modifier = Modifier.weight(1f),
                    text = confirmButtonText,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ReedConfirmDialogPreview() {
    ReedTheme {
        ReedDialog(
            title = "Title",
            confirmButtonText = "확인",
            onConfirmRequest = {},
            description = "subtext",
        )
    }
}

@Preview
@Composable
private fun ReedChoiceDialogPreview() {
    ReedTheme {
        ReedDialog(
            title = "Title",
            confirmButtonText = "확인",
            onConfirmRequest = {},
            description = "subtext",
            dismissButtonText = "취소",
        )
    }
}
