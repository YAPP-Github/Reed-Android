package com.ninecraft.booket.core.designsystem.component.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.utils.MultipleEventsCutter
import com.ninecraft.booket.core.common.utils.get
import com.ninecraft.booket.core.designsystem.ComponentPreview

@Composable
fun ReedButton(
    onClick: () -> Unit,
    sizeStyle: ButtonSizeStyle,
    colorStyle: ReedButtonColorStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
        label = "Scale Animation",
    )

    Button(
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        enabled = enabled,
        shape = RoundedCornerShape(sizeStyle.radius),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorStyle.containerColor(isPressed),
            contentColor = colorStyle.contentColor(),
            disabledContentColor = colorStyle.disabledContentColor(),
            disabledContainerColor = colorStyle.disabledContainerColor(),
        ),
        contentPadding = sizeStyle.paddingValues,
        interactionSource = interactionSource,
    ) {
        if (leadingIcon != null) {
            Box(Modifier.size(sizeStyle.iconSize)) {
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
            Box(Modifier.size(sizeStyle.iconSize)) {
                trailingIcon()
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ReedLargeButtonPreview() {
    Column(
        modifier = Modifier.padding(20.dp),
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
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.STROKE,
                sizeStyle = largeButtonStyle,
                text = "button",
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = largeRoundedButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.SECONDARY,
                sizeStyle = largeRoundedButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.TERTIARY,
                sizeStyle = largeRoundedButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.STROKE,
                sizeStyle = largeRoundedButtonStyle,
                text = "button",
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
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
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ReedButton(
                    onClick = {},
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    sizeStyle = largeRoundedButtonStyle,
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
                    sizeStyle = largeRoundedButtonStyle,
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
                    sizeStyle = largeRoundedButtonStyle,
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
    }
}

@ComponentPreview
@Composable
private fun ReedMediumButtonPreview() {
    Column(
        modifier = Modifier.padding(20.dp),
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = mediumRoundedButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.SECONDARY,
                sizeStyle = mediumRoundedButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.TERTIARY,
                sizeStyle = mediumRoundedButtonStyle,
                text = "button",
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
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
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ReedButton(
                    onClick = {},
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    sizeStyle = mediumRoundedButtonStyle,
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
                    sizeStyle = mediumRoundedButtonStyle,
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
                    sizeStyle = mediumRoundedButtonStyle,
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
    }
}

@ComponentPreview
@Composable
private fun ReedSmallButtonPreview() {
    Column(
        modifier = Modifier.padding(20.dp),
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = smallRoundedButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.SECONDARY,
                sizeStyle = smallRoundedButtonStyle,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.TERTIARY,
                sizeStyle = smallRoundedButtonStyle,
                text = "button",
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
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
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ReedButton(
                    onClick = {},
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    sizeStyle = smallRoundedButtonStyle,
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
                    sizeStyle = smallRoundedButtonStyle,
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
                    sizeStyle = smallRoundedButtonStyle,
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
    }
}

@ComponentPreview
@Composable
private fun ReedButtonDisabledPreview() {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = largeButtonStyle,
                enabled = false,
                text = "button",
            )
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = largeRoundedButtonStyle,
                enabled = false,
                text = "button",
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ReedButton(
                onClick = {},
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = largeButtonStyle,
                text = "button",
                enabled = false,
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
                colorStyle = ReedButtonColorStyle.PRIMARY,
                sizeStyle = largeRoundedButtonStyle,
                enabled = false,
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
}
