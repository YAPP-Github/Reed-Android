package com.ninecraft.booket.feature.search.component

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.BookSummaryModel

// 기기에서 평균적으로 한 화면에 보이는 아이템 개수
private const val LIMIT_COUNT = 6

@Composable
internal fun InfinityLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    loadMoreLimitCount: Int = LIMIT_COUNT,
    loadMore: () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    state.onLoadMore(limitCount = loadMoreLimitCount, action = loadMore)

    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content,
    )
}

@SuppressLint("ComposableNaming")
@Composable
private fun LazyListState.onLoadMore(
    limitCount: Int = 6,
    loadOnBottom: Boolean = true,
    action: () -> Unit,
) {
    val reached by remember {
        derivedStateOf {
            reachedBottom(limitCount = limitCount, triggerOnEnd = loadOnBottom)
        }
    }

    LaunchedEffect(reached) {
        if (reached) action()
    }
}

/**
 * @param limitCount: 몇 개의 아이템이 남았을 때 트리거 될 지에 대한 정보
 * @param triggerOnEnd: 바닥에 닿았을 때에도 트리거 할 지 여부
 *
 * @return 바닥에 닿았는지 여부(트리거 조건)
 */
private fun LazyListState.reachedBottom(
    limitCount: Int = LIMIT_COUNT,
    triggerOnEnd: Boolean = false,
): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return (triggerOnEnd && lastVisibleItem?.index == layoutInfo.totalItemsCount - 1) || lastVisibleItem?.index != 0 && lastVisibleItem?.index == layoutInfo.totalItemsCount - (limitCount + 1)
}

@Preview
@Composable
private fun InfinityLazyColumnPreview() {
    ReedTheme {
        Surface {
            var page by remember { mutableIntStateOf(1) }
            val contents = remember { mutableStateListOf(*Array(10) { it }) }

            Column {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Loaded Page: $page",
                    style = MaterialTheme.typography.headlineMedium,
                )
                InfinityLazyColumn(
                    loadMore = {
                        contents.addAll(page * 10 until (page + 1) * 10)
                        page++
                    },
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(contents, key = { it }) {
                            BookItem(
                                book = BookSummaryModel(
                                    title = "여름은 오래 그곳에 남아",
                                    author = "마쓰이에 마사시",
                                    publisher = "비채",
                                    coverImageUrl = "https://example.com/sample-book-cover.jpg",
                                    isbn = "",
                                ),
                                itemId = "1",
                                onBookClick = {},
                            )
                        }
                    },
                )
            }
        }
    }
}
