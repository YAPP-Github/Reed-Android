@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.hilt)
    alias(libs.plugins.booket.kotlin.library.serialization)
}

android {
    namespace = "com.ninecraft.booket.core.data.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "APP_VERSION", "\"${libs.versions.versionName.get()}\"")
    }
}

dependencies {
    implementations(
        projects.core.common,
        projects.core.data.api,
        projects.core.datastore.api,
        projects.core.model,
        projects.core.network,

        platform(libs.firebase.bom),
        libs.firebase.remote.config,
        libs.logger,
    )
}
