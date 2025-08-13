package com.ninecraft.booket.core.designsystem.component.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun TickOnlyCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check),
        contentDescription = "TickOnly Checkbox",
        modifier = modifier
            .size(24.dp)
            .noRippleClickable { onCheckedChange(!checked) },
        tint = if (checked) ReedTheme.colors.contentBrand else ReedTheme.colors.contentTertiary,
    )
}

@ComponentPreview
@Composable
private fun TickOnlyCheckBoxPreview() {
    ReedTheme {
        Row {
            var isChecked by remember { mutableStateOf(false) }

            TickOnlyCheckBox(
                checked = isChecked,
                onCheckedChange = { checked ->
                    isChecked = checked
                },
            )
        }
    }
}
