package com.ninecraft.booket.feature.main.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ninecraft.booket.feature.main.R
import com.ninecraft.booket.screens.HomeScreen
import com.ninecraft.booket.screens.LibraryScreen
import com.slack.circuit.runtime.screen.Screen

enum class MainTab(
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
    LIBRARY(
        iconResId = R.drawable.ic_library,
        selectedIconResId = R.drawable.ic_selected_library,
        labelResId = R.string.library_label,
        contentDescription = "Library Icon",
        screen = LibraryScreen,
    ),
}
