@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.booket.android.application)
    alias(libs.plugins.booket.android.application.compose)
    alias(libs.plugins.booket.android.hilt)
}

android {
    namespace = "com.ninecraft.booket"

    defaultConfig {
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getApiKey("KAKAO_NATIVE_APP_KEY"))
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getApiKey("KAKAO_NATIVE_APP_KEY").trim('"')
    }

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
        projects.core.common,
        projects.core.data.api,
        projects.core.data.impl,
        projects.core.datastore.api,
        projects.core.datastore.impl,
        projects.core.designsystem,
        projects.core.model,
        projects.core.network,
        projects.core.ui,

        projects.feature.home,
        projects.feature.library,
        projects.feature.login,
        projects.feature.main,
        projects.feature.screens,
        projects.feature.search,
        projects.feature.settings,
        projects.feature.webview,
        projects.feature.detail,

        libs.androidx.activity.compose,
        libs.androidx.startup,
        libs.coil.compose,
        libs.kakao.auth,
        libs.logger,

        libs.bundles.circuit,
    )
    api(libs.circuit.codegen.annotation)
    ksp(libs.circuit.codegen.ksp)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
