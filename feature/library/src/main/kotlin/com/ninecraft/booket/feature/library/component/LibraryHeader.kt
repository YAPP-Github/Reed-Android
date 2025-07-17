package com.ninecraft.booket.feature.library.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White

@Composable
fun LibraryHeader(
    onSearchClick: () -> Unit,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing5))
        Text(
            text = stringResource(com.ninecraft.booket.feature.library.R.string.library_title),
            modifier = Modifier.weight(1f),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.heading1Bold,
        )
        IconButton(
            onClick = { onSearchClick() },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
            )
        }
        IconButton(
            onClick = { onSettingClick() },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                contentDescription = "Settings Icon",
            )
        }
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
    }
}

@Preview
@Composable
private fun LibraryHeaderPreview() {
    ReedTheme {
        LibraryHeader(
            onSearchClick = {},
            onSettingClick = {},
        )
    }
}
