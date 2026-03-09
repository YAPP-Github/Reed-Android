plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.metro)
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
        projects.core.di,
        projects.core.model,
        projects.core.network,

        platform(libs.firebase.bom),
        libs.firebase.remote.config,
        libs.firebase.messaging,
        libs.logger,
    )
}
