@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.library.compose)
}

android {
    namespace = "com.ninecraft.booket.core.designsystem"
}

dependencies {
    implementations(
        libs.logger,
    )
}
