@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.hilt)
    alias(libs.plugins.booket.kotlin.library.serialization)
}

android {
    namespace = "com.ninecraft.booket.core.datastore.impl"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementations(
        projects.core.datastore.api,
        projects.core.model,

        libs.androidx.datastore.preferences,

        libs.logger,
    )

    androidTestImplementations(
        libs.androidx.test.ext.junit,
        libs.androidx.test.runner,
        libs.kotlinx.coroutines.test,
    )
}
