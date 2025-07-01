@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.library.compose)
    alias(libs.plugins.booket.android.hilt)
    alias(libs.plugins.booket.android.retrofit)
}

android {
    namespace = "com.ninecraft.booket.core.common"
}

dependencies {
    implementations(
        projects.core.model,

        libs.logger,
    )
}
