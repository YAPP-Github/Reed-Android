import com.ninecraft.booket.convention.getLocalProperty


plugins {
    alias(libs.plugins.booket.android.library)
    alias(libs.plugins.booket.android.retrofit)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.ninecraft.booket.core.network"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "SERVER_BASE_URL", getLocalProperty("DEBUG_SERVER_BASE_URL"))
        }

        release {
            buildConfigField("String", "SERVER_BASE_URL", getLocalProperty("RELEASE_SERVER_BASE_URL"))
        }
    }
}

dependencies {
    implementations(
        projects.core.datastore.api,
        projects.core.di,

        libs.kotlinx.coroutines.core,
        libs.logger,
    )
}
