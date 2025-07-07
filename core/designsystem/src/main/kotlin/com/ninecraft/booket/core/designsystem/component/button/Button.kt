package com.ninecraft.booket.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.utils.MultipleEventsCutter
import com.ninecraft.booket.core.common.utils.get
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun BooketButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    disabledContainerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    disabledContentColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Button(
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        ),
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else {
            ButtonDefaults.ContentPadding
        },
    ) {
        BooketButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
private fun BooketButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier.padding(
            start = if (leadingIcon != null) {
                ButtonDefaults.IconSpacing
            } else {
                0.dp
            },
        ),
    ) {
        text()
    }
}

@ComponentPreview
@Composable
private fun BooketButtonPreview() {
    ReedTheme {
        BooketButton(
            onClick = {},
            text = {
                Text(text = "Button")
            },
        )
    }
}

@ComponentPreview
@Composable
private fun BooketButtonWithLeadingIconPreview() {
    ReedTheme {
        BooketButton(
            onClick = {},
            text = {
                Text("Check Button")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                    tint = Color.White,
                )
            },
        )
    }
}
