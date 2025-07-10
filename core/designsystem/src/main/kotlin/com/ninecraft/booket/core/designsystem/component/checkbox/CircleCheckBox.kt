package com.ninecraft.booket.core.designsystem.component.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun CircleCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (checked) ReedTheme.colors.bgPrimary else ReedTheme.colors.basePrimary
    val borderColor = if (checked) Color.Transparent else ReedTheme.colors.borderPrimary
    val iconTint = if (checked) ReedTheme.colors.contentInverse else ReedTheme.colors.contentTertiary

    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                color = bgColor,
                shape = CircleShape,
            )
            .border(1.dp, borderColor, CircleShape)
            .noRippleClickable { onCheckedChange(!checked) }
            .padding(2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_check),
            contentDescription = "Circle Checkbox",
            tint = iconTint,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleCheckboxPreview() {
    ReedTheme {
        var isChecked by remember { mutableStateOf(false) }

        CircleCheckBox(
            checked = isChecked,
            onCheckedChange = { checked ->
                isChecked = checked
            },
        )
    }
}
