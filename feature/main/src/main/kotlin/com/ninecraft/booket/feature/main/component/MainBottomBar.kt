package com.ninecraft.booket.feature.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.screens.HomeScreen
import com.ninecraft.booket.screens.LibraryScreen
import com.ninecraft.booket.screens.SearchScreen
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainBottomBar(
    navigator: Navigator,
    backStack: SaveableBackStack,
    modifier: Modifier = Modifier,
) {
    val visible = shouldShowBottomBar(backStack)
    val currentTab = getCurrentTab(backStack)
    val tabs = MainTab.entries.toImmutableList()

    MainBottomBar(
        visible = visible,
        tabs = tabs,
        currentTab = currentTab,
        onTabSelected = { tab ->
            navigator.popUntilOrGoTo(tab.screen)
        },
        modifier = modifier,
    )
}

@Composable
internal fun MainBottomBar(
    visible: Boolean,
    tabs: ImmutableList<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) },
        modifier = modifier,
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            Column {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    tabs.forEach { tab ->
                        MainBottomBarItem(
                            tab = tab,
                            selected = tab == currentTab,
                            onClick = {
                                if (tab != currentTab) {
                                    onTabSelected(tab)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = if (selected) ImageVector.vectorResource(tab.selectedIconResId)
                else ImageVector.vectorResource(tab.iconResId),
                contentDescription = tab.contentDescription,
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(tab.labelResId),
                color = if (selected) Color(0xFF1F1F1F) else Color(0xFF9E9E9E),
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun MainBottomBarPreview() {
    ReedTheme {
        MainBottomBar(
            visible = true,
            tabs = MainTab.entries.toImmutableList(),
            currentTab = MainTab.HOME,
            onTabSelected = {},
        )
    }
}

fun Navigator.popUntilOrGoTo(screen: Screen) {
    if (screen in peekBackStack()) {
        popUntil { it == screen }
    } else {
        goTo(screen)
    }
}

private val mainBottomBarScreens = setOf(HomeScreen, SearchScreen, LibraryScreen)

@Composable
private fun shouldShowBottomBar(backStack: SaveableBackStack): Boolean {
    val currentScreen = backStack.topRecord?.screen
    return currentScreen in mainBottomBarScreens
}

@Composable
private fun getCurrentTab(backStack: SaveableBackStack): MainTab? {
    val currentScreen = backStack.topRecord?.screen
    return currentScreen?.let { screen ->
        MainTab.entries.find { it.screen::class == currentScreen::class }
    }
}
