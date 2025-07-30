package com.ninecraft.booket.feature.termsagreement.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.checkbox.TickOnlyCheckBox
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun TermItem(
    title: String,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    hasDetailAction: Boolean = true,
    onDetailClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onDetailClick() }
            .padding(
                start = ReedTheme.spacing.spacing4 + ReedTheme.spacing.spacing05,
                end = ReedTheme.spacing.spacing3,
                top = ReedTheme.spacing.spacing2,
                bottom = ReedTheme.spacing.spacing2,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TickOnlyCheckBox(
            checked = checked,
            onCheckedChange = { onCheckClick() },
        )
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing3 + ReedTheme.spacing.spacing05))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )

        if (hasDetailAction) {
            Icon(
                imageVector = ImageVector.vectorResource(id = designR.drawable.ic_chevron_right),
                contentDescription = "Navigation Icon",
                tint = Color.Unspecified,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun TermItemPreview() {
    ReedTheme {
        TermItem(
            title = "(필수)서비스 이용약관",
            onCheckClick = {}
        )
    }
}
