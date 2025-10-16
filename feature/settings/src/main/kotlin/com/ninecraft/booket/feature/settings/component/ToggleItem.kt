package com.ninecraft.booket.feature.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
internal fun ToggleItem(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = ReedTheme.spacing.spacing4,
                horizontal = ReedTheme.spacing.spacing5,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = title,
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.body1Medium,
            )
            Text(
                text = description,
                color = ReedTheme.colors.contentTertiary,
                style = ReedTheme.typography.label1Medium,
            )
        }
        ReedSwitch(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(!isChecked)
            },
        )
    }
}

@DevicePreview
@Composable
private fun ToggleItemPreview() {
    ReedTheme {
        ToggleItem(
            title = "알림 받기",
            description = "리드에서 알림을 보내드려요",
            isChecked = true,
            onCheckedChange = {},
        )
    }
}
