package com.ninecraft.booket.feature.search.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
fun SearchItem(
    query: String,
    onQueryClick: (String) -> Unit,
    onDeleteIconClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onQueryClick(query) }
            .padding(
                horizontal = ReedTheme.spacing.spacing6,
                vertical = ReedTheme.spacing.spacing4,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = query,
            color = ReedTheme.colors.contentSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = ReedTheme.typography.body1Medium,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing3))
        Icon(
            imageVector = ImageVector.vectorResource(id = designR.drawable.ic_close),
            contentDescription = "Remove Icon",
            tint = ReedTheme.colors.contentSecondary,
            modifier = Modifier
                .size(18.dp)
                .clickable { onDeleteIconClick(query) },
        )
    }
}

@ComponentPreview
@Composable
private fun SearchItemPreview() {
    ReedTheme {
        SearchItem(
            query = "최근 검색어 최근 검색어 최근 검색어 최근 검색어 최근 검색어",
            onQueryClick = {},
            onDeleteIconClick = {},
        )
    }
}
