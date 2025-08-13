package com.ninecraft.booket.feature.screens

import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

abstract class ReedScreen(val name: String) : Screen {
    override fun toString(): String = name
}

@Parcelize
data object HomeScreen : ReedScreen(name = "Home()")

@Parcelize
data object LibraryScreen : ReedScreen(name = "Library()")

@Parcelize
data object LoginScreen : ReedScreen(name = "Login()")

@Parcelize
data object SearchScreen : ReedScreen(name = "Search()")

@Parcelize
data object LibrarySearchScreen : ReedScreen(name = "LibrarySearch()")

@Parcelize
data object TermsAgreementScreen : ReedScreen(name = "TermsAgreement()")

@Parcelize
data object SettingsScreen : ReedScreen(name = "Settings()")

@Parcelize
data object OssLicensesScreen : ReedScreen(name = "OssLicenses()")

@Parcelize
data class RecordScreen(val userBookId: String) : ReedScreen(name = "Record")

@Parcelize
data object OcrScreen : ReedScreen(name = "Ocr()") {
    @Parcelize
    data class OcrResult(val sentence: String) : PopResult
}

@Parcelize
data class RecordDetailScreen(val recordId: String) : ReedScreen(name = "RecordDetail()")

@Parcelize
data class WebViewScreen(
    val url: String,
    val title: String,
) : ReedScreen(name = "WebView()")

@Parcelize
data class BookDetailScreen(
    val userBookId: String,
    val isbn13: String,
) : ReedScreen(name = "BookDetail()")

@Parcelize
data object OnboardingScreen : ReedScreen(name = "Onboarding()")

@Parcelize
data object SplashScreen : ReedScreen(name = "Splash()")
