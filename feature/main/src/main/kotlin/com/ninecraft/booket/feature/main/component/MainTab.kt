package com.ninecraft.booket.feature.main.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ninecraft.booket.feature.home.HomeScreen
import com.ninecraft.booket.feature.library.LibraryScreen
import com.ninecraft.booket.feature.main.R
import com.ninecraft.booket.feature.search.SearchScreen
import com.slack.circuit.runtime.screen.Screen

internal enum class MainTab(
    @DrawableRes val iconResId: Int,
    @DrawableRes val selectedIconResId: Int,
    @StringRes val labelResId: Int,
    internal val contentDescription: String,
    val screen: Screen,
) {
    HOME(
        iconResId = R.drawable.ic_home,
        selectedIconResId = R.drawable.ic_selected_home,
        labelResId = R.string.home_label,
        contentDescription = "Home Icon",
        screen = HomeScreen,
    ),
    SEARCH(
        iconResId = R.drawable.ic_search,
        selectedIconResId = R.drawable.ic_selected_search,
        labelResId = R.string.search_label,
        contentDescription = "Search Icon",
        screen = SearchScreen,
    ),
    LIBRARY(
        iconResId = R.drawable.ic_library,
        selectedIconResId = R.drawable.ic_selected_library,
        labelResId = R.string.library_label,
        contentDescription = "Library Icon",
        screen = LibraryScreen,
    ),
}
