@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.booket.android.application)
    alias(libs.plugins.booket.android.application.compose)
    alias(libs.plugins.booket.android.hilt)
    alias(libs.plugins.booket.android.firebase)
}

android {
    namespace = "com.ninecraft.booket"

    signingConfigs {
        create("release") {
            val propertiesFile = rootProject.file("keystore.properties")
            val properties = Properties()
            properties.load(propertiesFile.inputStream())
            storeFile = rootProject.file(properties["STORE_FILE"] as String)
            storePassword = properties["STORE_PASSWORD"] as String
            keyAlias = properties["KEY_ALIAS"] as String
            keyPassword = properties["KEY_PASSWORD"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".dev"
            manifestPlaceholders += mapOf(
                "appName" to "@string/app_name_dev",
            )
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders += mapOf(
                "appName" to "@string/app_name",
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    defaultConfig {
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getApiKey("KAKAO_NATIVE_APP_KEY"))
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getApiKey("KAKAO_NATIVE_APP_KEY").trim('"')
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
        projects.core.ocr,

        projects.feature.detail,
        projects.feature.home,
        projects.feature.library,
        projects.feature.login,
        projects.feature.main,
        projects.feature.onboarding,
        projects.feature.record,
        projects.feature.screens,
        projects.feature.search,
        projects.feature.settings,
        projects.feature.splash,
        projects.feature.webview,
        projects.feature.edit,

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
