package com.ninecraft.booket.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun ReedDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(ReedTheme.colors.dividerMd),
    )
}

@Preview
@Composable
private fun ReedDividerPreview() {
    ReedTheme {
        ReedDivider()
    }
}
