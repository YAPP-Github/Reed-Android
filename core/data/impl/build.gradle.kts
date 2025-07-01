@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.hilt)
    alias(libs.plugins.booket.kotlin.library.serialization)
}

android {
    namespace = "com.ninecraft.booket.core.data.impl"
}

dependencies {
    implementations(
        projects.core.common,
        projects.core.data.api,
        projects.core.datastore,
        projects.core.model,
        projects.core.network,

        libs.logger,
    )
}
