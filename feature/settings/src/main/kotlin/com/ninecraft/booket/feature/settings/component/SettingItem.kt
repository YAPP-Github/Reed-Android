package com.ninecraft.booket.feature.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
internal fun SettingItem(
    title: String,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    onItemClick: () -> Unit = {},
    action: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
) {
    val combinedModifier = if (isClickable) {
        modifier
            .fillMaxWidth()
            .clickableSingle { onItemClick() }
    } else {
        modifier.fillMaxWidth()
    }

    Column {
        Row(
            modifier = combinedModifier
                .padding(
                    horizontal = ReedTheme.spacing.spacing5,
                    vertical = ReedTheme.spacing.spacing4,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = ReedTheme.typography.body1Medium,
                    color = ReedTheme.colors.contentPrimary,
                )
                description()
            }
            action()
        }
    }
}

@ComponentPreview
@Composable
private fun SettingItemPreview() {
    ReedTheme {
        SettingItem(
            title = "로그아웃",
        )
    }
}
