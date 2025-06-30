@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
}

android {
    namespace = "com.ninecraft.booket.core.data.api"
}

dependencies {
    implementations(
        projects.core.model,

        libs.logger,
    )
}
