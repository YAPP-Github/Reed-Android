@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.library.compose)
}

android {
    namespace = "com.ninecraft.booket.core.ui"
}

dependencies {
    implementations(
        projects.core.designsystem,
        projects.core.common,

        libs.compose.keyboard.state,
        libs.logger,
    )
}
