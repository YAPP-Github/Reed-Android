package com.ninecraft.booket.feature.search.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.search.R

@Composable
internal fun RecentSearchTitle(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReedTheme.spacing.spacing5, vertical = ReedTheme.spacing.spacing2),
    ) {
        Text(
            text = stringResource(R.string.recent_search),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1SemiBold,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
    }
}

@ComponentPreview
@Composable
private fun RecentSearchTitlePreview() {
    ReedTheme {
        RecentSearchTitle()
    }
}
