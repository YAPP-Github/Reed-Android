package com.ninecraft.booket.feature.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.adamglin.composeshadow.dropShadow
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainBottomBar(
    tabs: ImmutableList<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .dropShadow(
                shape = RoundedCornerShape(
                    topStart = ReedTheme.spacing.spacing3,
                    topEnd = ReedTheme.spacing.spacing3,
                ),
                color = ReedTheme.colors.borderPrimary.copy(alpha = 0.05f),
                offsetY = (-4).dp,
                blur = 20.dp,
            )
            .clip(
                RoundedCornerShape(
                    topStart = ReedTheme.spacing.spacing3,
                    topEnd = ReedTheme.spacing.spacing3,
                ),
            )
            .border(
                width = 1.dp,
                color = ReedTheme.colors.borderPrimary,
                shape = RoundedCornerShape(
                    topStart = ReedTheme.spacing.spacing3,
                    topEnd = ReedTheme.spacing.spacing3,
                ),
            )
            .background(White),
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(58.dp),
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
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            Icon(
                imageVector = if (selected) ImageVector.vectorResource(tab.selectedIconResId)
                else ImageVector.vectorResource(tab.iconResId),
                contentDescription = tab.contentDescription,
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            Text(
                text = stringResource(tab.labelResId),
                color = if (selected) ReedTheme.colors.contentPrimary else ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.caption2Regular,
            )
        }
    }
}

@Suppress("unused")
fun Navigator.popUntilOrGoTo(screen: Screen) {
    if (screen in peekBackStack()) {
        popUntil { it == screen }
    } else {
        goTo(screen)
    }
}

@Composable
fun getCurrentTab(backStack: SaveableBackStack): MainTab? {
    val currentScreen = backStack.topRecord?.screen
    return currentScreen?.let { screen ->
        MainTab.entries.find { it.screen::class == currentScreen::class }
    }
}

@ComponentPreview
@Composable
private fun MainBottomBarPreview() {
    ReedTheme {
        MainBottomBar(
            tabs = MainTab.entries.toImmutableList(),
            currentTab = MainTab.HOME,
            onTabSelected = {},
        )
    }
}
