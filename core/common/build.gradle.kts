@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.library.compose)
    alias(libs.plugins.booket.android.hilt)
    alias(libs.plugins.booket.android.retrofit)
}

android {
    namespace = "com.ninecraft.booket.core.common"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "PACKAGE_NAME", "\"${libs.versions.packageName.get()}\"")
    }
}

dependencies {
    implementations(
        projects.core.model,
        projects.core.network,

        libs.kotlinx.collections.immutable,

        platform(libs.firebase.bom),
        libs.firebase.analytics,
        libs.logger,
    )
}
