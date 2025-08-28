package com.ninecraft.booket.core.designsystem.component.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.utils.MultipleEventsCutter
import com.ninecraft.booket.core.common.utils.get
import com.ninecraft.booket.core.designsystem.ComponentPreview

@Composable
fun ReedTextButton(
    onClick: () -> Unit,
    text: String,
    sizeStyle: ButtonSizeStyle,
    colorStyle: ReedButtonColorStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    multipleEventsCutterEnabled: Boolean = true,
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TextButton(
        onClick = {
            if (multipleEventsCutterEnabled) {
                multipleEventsCutter.processEvent { onClick() }
            } else {
                onClick()
            }
        },
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            containerColor = colorStyle.containerColor(isPressed),
            contentColor = colorStyle.contentColor(),
            disabledContentColor = colorStyle.disabledContentColor(),
            disabledContainerColor = colorStyle.disabledContainerColor(),
        ),
        contentPadding = sizeStyle.paddingValues,
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = text,
                style = sizeStyle.textStyle.copy(
                    color = if (enabled) colorStyle.contentColor() else colorStyle.disabledContentColor(),
                ),
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = if (enabled) colorStyle.contentColor() else Color.Transparent,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ReedTextButtonPreview() {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedTextButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.TEXT,
                sizeStyle = largeButtonStyle,
                text = "text button",
            )

            ReedTextButton(
                onClick = {},
                enabled = false,
                colorStyle = ReedButtonColorStyle.TEXT,
                sizeStyle = largeButtonStyle,
                text = "text button",
            )
        }
    }
}
