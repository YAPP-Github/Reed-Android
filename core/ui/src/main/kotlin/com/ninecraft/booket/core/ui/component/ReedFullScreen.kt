package com.ninecraft.booket.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ninecraft.booket.core.designsystem.theme.White

/**
 * systemBarsPadding() 이 자동으로 적용되는 전체 화면 컨테이너
 * Scaffold 밖에 있는 화면에서 사용
 *
 * @param backgroundColor 화면 배경 색상
 */
@Composable
fun ReedFullScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = White,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding(),
    ) {
        content()
    }
}
