@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    alias(libs.plugins.booket.android.application)
    alias(libs.plugins.booket.android.hilt)
    alias(libs.plugins.booket.android.application.compose)
}

android {
    namespace = "com.ninecraft.booket"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementations(
        projects.core.designsystem,

        projects.feature.main,

        libs.androidx.activity.compose,
        libs.androidx.startup,
        libs.logger,

        libs.bundles.circuit,
    )
    api(libs.circuit.codegen.annotation)
    ksp(libs.circuit.codegen.ksp)
}
