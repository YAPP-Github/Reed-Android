package com.ninecraft.booket.core.designsystem.component.textfield

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun ReedRecordTextField(
    recordState: TextFieldState,
    @StringRes recordHintRes: Int,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done,
    ),
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.MultiLine(),
    onClear: (() -> Unit)? = null,
    onNext: () -> Unit = {},
    backgroundColor: Color = ReedTheme.colors.baseSecondary,
    textColor: Color = ReedTheme.colors.contentPrimary,
    cornerShape: RoundedCornerShape = RoundedCornerShape(ReedTheme.radius.sm),
    borderStroke: BorderStroke = BorderStroke(width = 1.dp, color = ReedTheme.colors.baseSecondary),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    CompositionLocalProvider(LocalTextSelectionColors provides reedTextSelectionColors) {
        BasicTextField(
            state = recordState,
            modifier = Modifier.fillMaxWidth(),
            textStyle = ReedTheme.typography.body2Medium.copy(color = textColor),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = {
                if (keyboardOptions.imeAction == ImeAction.Next) {
                    onNext()
                } else {
                    keyboardController?.hide()
                }
            },
            lineLimits = lineLimits,
            decorator = { innerTextField ->
                Row(
                    modifier = modifier
                        .background(color = backgroundColor, shape = cornerShape)
                        .border(
                            border = borderStroke,
                            shape = cornerShape,
                        )
                        .padding(vertical = ReedTheme.spacing.spacing3),
                    verticalAlignment = if (lineLimits is TextFieldLineLimits.MultiLine) Alignment.Top else Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing4))
                    Box(modifier = Modifier.weight(1f)) {
                        if (recordState.text.isEmpty()) {
                            Text(
                                text = stringResource(id = recordHintRes),
                                color = ReedTheme.colors.contentTertiary,
                                style = ReedTheme.typography.body2Regular,
                            )
                        }
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                    if (recordState.text.toString().isNotEmpty() && onClear != null) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_x_circle),
                            contentDescription = "Clear Icon",
                            modifier = Modifier.clickable {
                                onClear()
                            },
                            tint = Color.Unspecified,
                        )
                    }
                    Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing4))
                }
            },
        )
    }
}

@ComponentPreview
@Composable
private fun ReedRecordTextFieldPreview() {
    ReedTheme {
        ReedRecordTextField(
            recordState = TextFieldState("검색"),
            recordHintRes = R.string.search_book_hint,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            onClear = {},
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
    }
}
