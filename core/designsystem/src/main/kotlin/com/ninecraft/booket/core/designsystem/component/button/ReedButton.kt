package com.ninecraft.booket.core.designsystem.component.button

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.utils.MultipleEventsCutter
import com.ninecraft.booket.core.common.utils.get

@Composable
fun ReedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorStyle: ReedButtonColorStyle,
    sizeStyle: ButtonSizeStyle,
    text: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "ScaleAnimation",
    )

    Button(
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        enabled = enabled,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(sizeStyle.radius),
        contentPadding = sizeStyle.paddingValues,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorStyle.containerColor(isPressed),
            contentColor = colorStyle.contentColor(),
            disabledContentColor = colorStyle.disabledContentColor(),
            disabledContainerColor = colorStyle.disabledContainerColor(),
        ),
    ) {
        if (leadingIcon != null) {
            Box(Modifier.sizeIn(maxHeight = 24.dp)) {
                leadingIcon()
            }
        }

        if (leadingIcon != null && text.isNotEmpty()) {
            Spacer(Modifier.width(sizeStyle.iconSpacing))
        }

        Text(
            text = text,
            style = sizeStyle.textStyle.copy(
                color = if (enabled) colorStyle.contentColor() else colorStyle.disabledContentColor(),
            ),
        )

        if (trailingIcon != null && text.isNotEmpty()) {
            Spacer(Modifier.width(sizeStyle.iconSpacing))
        }

        if (trailingIcon != null) {
            Box(Modifier.sizeIn(maxHeight = 24.dp)) {
                trailingIcon()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReedLargeButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = largeButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.SECONDARY,
                sizeStyle = largeButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.TERTIARY,
                sizeStyle = largeButtonStyle,
                text = "button",
            )
        }
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.SECONDARY,
            sizeStyle = largeButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.TERTIARY,
            sizeStyle = largeButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReedMediumButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = mediumButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.SECONDARY,
                sizeStyle = mediumButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.TERTIARY,
                sizeStyle = mediumButtonStyle,
                text = "button",
            )
        }
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = mediumButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.SECONDARY,
            sizeStyle = mediumButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.TERTIARY,
            sizeStyle = mediumButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReedSmallButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = smallButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.SECONDARY,
                sizeStyle = smallButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.TERTIARY,
                sizeStyle = smallButtonStyle,
                text = "button",
            )
        }
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = smallButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.SECONDARY,
            sizeStyle = smallButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.TERTIARY,
            sizeStyle = smallButtonStyle,
            text = "button",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            },
        )
    }
}
