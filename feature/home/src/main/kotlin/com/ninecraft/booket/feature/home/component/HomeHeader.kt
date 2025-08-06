package com.ninecraft.booket.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.ResourceImage
import com.ninecraft.booket.core.designsystem.R as designR
import com.ninecraft.booket.core.designsystem.theme.HomeBg
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.home.R

@Composable
fun HomeHeader(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HomeBg)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing5))
        ResourceImage(
            imageRes = R.drawable.reed_logo,
            contentDescription = "Reed Logo",
            modifier = Modifier
                .width(65.dp)
                .height(24.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                onSettingsClick()
            },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = designR.drawable.ic_settings),
                contentDescription = "Settings Icon",
            )
        }
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
    }
}

@ComponentPreview
@Composable
private fun HomeHeaderPreview() {
    ReedTheme {
        HomeHeader(
            onSettingsClick = {},
        )
    }
}
