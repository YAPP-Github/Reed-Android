package com.ninecraft.booket.feature.search.book.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedBottomSheet
import com.ninecraft.booket.feature.search.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRegisterSuccessBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    upsertedBookStatus: BookStatus,
    onCancelButtonClick: () -> Unit,
    onOKButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Image(
                painter = painterResource(R.drawable.img_book_register_complete),
                contentDescription = "Book Register Complete Image",
                modifier = Modifier.height(120.dp),
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
            Text(
                text = stringResource(R.string.book_register_success_title),
                modifier = Modifier.fillMaxWidth(),
                color = ReedTheme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.heading2SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            Text(
                text = stringResource(
                    when (upsertedBookStatus) {
                        BookStatus.BEFORE_READING -> R.string.book_register_success_description_before_reading
                        BookStatus.READING -> R.string.book_register_success_description
                        BookStatus.COMPLETED -> R.string.book_register_success_description_completed
                    },
                ),
                modifier = Modifier.fillMaxWidth(),
                color = ReedTheme.colors.contentSecondary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))

            if (upsertedBookStatus == BookStatus.BEFORE_READING) {
                ReedButton(
                    onClick = {
                        onCancelButtonClick()
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = ReedTheme.spacing.spacing4),
                    text = stringResource(R.string.book_register_success_ok_before_reading),
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = ReedTheme.spacing.spacing4),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ReedButton(
                        onClick = {
                            onCancelButtonClick()
                        },
                        sizeStyle = largeButtonStyle,
                        colorStyle = ReedButtonColorStyle.SECONDARY,
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.book_register_success_cancel),
                    )
                    Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                    ReedButton(
                        onClick = {
                            onOKButtonClick()
                        },
                        sizeStyle = largeButtonStyle,
                        colorStyle = ReedButtonColorStyle.PRIMARY,
                        modifier = Modifier.weight(1f),
                        text = if (upsertedBookStatus == BookStatus.READING) {
                            stringResource(R.string.book_register_success_ok)
                        } else {
                            stringResource(R.string.book_register_success_ok_completed)
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun BookRegisterSuccessBeforeReadingBottomSheetPreview() {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )
    ReedTheme {
        BookRegisterSuccessBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            upsertedBookStatus = BookStatus.BEFORE_READING,
            onCancelButtonClick = {},
            onOKButtonClick = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun BookRegisterSuccessReadingBottomSheetPreview() {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )
    ReedTheme {
        BookRegisterSuccessBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            upsertedBookStatus = BookStatus.READING,
            onCancelButtonClick = {},
            onOKButtonClick = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun BookRegisterSuccessCompletedBottomSheetPreview() {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )
    ReedTheme {
        BookRegisterSuccessBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            upsertedBookStatus = BookStatus.COMPLETED,
            onCancelButtonClick = {},
            onOKButtonClick = {},
        )
    }
}
