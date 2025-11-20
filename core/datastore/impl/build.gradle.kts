@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.metro)
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
        projects.core.common,
        projects.core.datastore.api,
        projects.core.di,
        projects.core.model,

        libs.logger,
    )

    // API because DataStore<Preferences> is exposed in public API (DataStoreGraph)
    // Metro compiler needs to resolve Preferences type across modules
    // See: https://github.com/ZacSweers/metro/discussions/1358#discussioncomment-15020091
    api(libs.androidx.datastore.preferences)

    androidTestImplementations(
        libs.androidx.test.ext.junit,
        libs.androidx.test.runner,
        libs.kotlinx.coroutines.test,
    )
}
