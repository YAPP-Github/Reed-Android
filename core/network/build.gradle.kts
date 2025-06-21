@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties


plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.retrofit)
    alias(libs.plugins.booket.android.hilt)
}

android {
    namespace = "com.ninecraft.booket.core.network"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "SERVER_BASE_URL", getServerBaseUrl("DEBUG_SERVER_BASE_URL"))
        }

        getByName("release") {
            buildConfigField("String", "SERVER_BASE_URL", getServerBaseUrl("RELEASE_SERVER_BASE_URL"))
        }
    }
}

dependencies {
    implementations(
        libs.logger,
    )
}

fun getServerBaseUrl(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
