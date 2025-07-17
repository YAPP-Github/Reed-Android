package com.ninecraft.booket.feature.main.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ninecraft.booket.feature.main.R
import com.ninecraft.booket.screens.HomeScreen
import com.ninecraft.booket.screens.LibraryScreen
import com.ninecraft.booket.screens.SearchScreen
import com.slack.circuit.runtime.screen.Screen

enum class MainTab(
    @DrawableRes val iconResId: Int,
    @DrawableRes val selectedIconResId: Int,
    @StringRes val labelResId: Int,
    internal val contentDescription: String,
    val screen: Screen,
) {
    HOME(
        iconResId = R.drawable.ic_home_tab,
        selectedIconResId = R.drawable.ic_selected_home_tab,
        labelResId = R.string.home_label,
        contentDescription = "Home Icon",
        screen = HomeScreen,
    ),
    SEARCH(
        iconResId = R.drawable.ic_search_tab,
        selectedIconResId = R.drawable.ic_selected_search_tab,
        labelResId = R.string.search_label,
        contentDescription = "Search Icon",
        screen = SearchScreen,
    ),
    LIBRARY(
        iconResId = R.drawable.ic_library_tab,
        selectedIconResId = R.drawable.ic_selected_library_tab,
        labelResId = R.string.library_label,
        contentDescription = "Library Icon",
        screen = LibraryScreen,
    ),
}
