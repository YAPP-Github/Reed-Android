package com.ninecraft.booket.feature.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

abstract class ReedScreen(val name: String) : Screen {
    override fun toString(): String = name
}

@Parcelize
data object BottomNavigationScreen : ReedScreen(name = "BottomNavigation()")

@Parcelize
data object HomeScreen : ReedScreen(name = "Home()")

@Parcelize
data object LibraryScreen : ReedScreen(name = "Library()")

@Parcelize
data object LoginScreen : ReedScreen(name = "Login()")

@Parcelize
data object SearchScreen : ReedScreen(name = "Search()")

@Parcelize
data object TermsAgreementScreen : ReedScreen(name = "TermsAgreement()")

@Parcelize
data object SettingsScreen : ReedScreen(name = "Settings()")

@Parcelize
data object OssLicensesScreen : ReedScreen(name = "OssLicenses()")

@Parcelize
data object RecordScreen : ReedScreen(name = "Record()")

@Parcelize
data object OcrScreen : ReedScreen(name = "Ocr()")

@Parcelize
data class WebViewScreen(
    val url: String,
    val title: String,
) : ReedScreen(name = "WebView()")

@Parcelize
data class BookDetailScreen(val isbn: String) : ReedScreen(name = "BookDetail()")
