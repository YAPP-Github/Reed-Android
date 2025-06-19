package com.ninecraft.booket.feature.main.component

import androidx.annotation.DrawableRes
import com.ninecraft.booket.feature.home.HomeScreen
import com.ninecraft.booket.feature.library.LibraryScreen
import com.ninecraft.booket.feature.main.R
import com.ninecraft.booket.feature.search.SearchScreen
import com.slack.circuit.runtime.screen.Screen

internal enum class MainTab(
    @DrawableRes val iconResId: Int,
    @DrawableRes val selectedIconResId: Int,
    internal val contentDescription: String,
    val label: String,
    val screen: Screen,
) {
    HOME(
        iconResId = R.drawable.ic_home,
        selectedIconResId = R.drawable.ic_selected_home,
        contentDescription = "Home Icon",
        label = "홈",
        screen = HomeScreen,
    ),
    SEARCH(
        iconResId = R.drawable.ic_search,
        selectedIconResId = R.drawable.ic_selected_search,
        contentDescription = "Search Icon",
        label = "도서 검색",
        screen = SearchScreen,
    ),
    LIBRARY(
        iconResId = R.drawable.ic_library,
        selectedIconResId = R.drawable.ic_selected_library,
        contentDescription = "Library Icon",
        label = "내 서재",
        screen = LibraryScreen,
    ),
}
