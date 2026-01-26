@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import com.google.devtools.ksp.gradle.KspExtension
import com.ninecraft.booket.convention.getLocalProperty
import org.gradle.kotlin.dsl.configure
import java.util.Properties

plugins {
    alias(libs.plugins.booket.android.application)
    alias(libs.plugins.booket.android.application.compose)
    alias(libs.plugins.booket.android.firebase)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
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
            buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", getLocalProperty("DEBUG_GOOGLE_WEB_CLIENT_ID"))
            manifestPlaceholders += mapOf(
                "appName" to "@string/app_name_dev",
            )
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", getLocalProperty("RELEASE_GOOGLE_WEB_CLIENT_ID"))
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
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getLocalProperty("KAKAO_NATIVE_APP_KEY"))
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getLocalProperty("KAKAO_NATIVE_APP_KEY").trim('"')
    }

    buildFeatures {
        buildConfig = true
    }
}

composeStabilityAnalyzer {
    enabled.set(true)
}

extensions.configure<KspExtension> {
    arg("circuit.codegen.mode", "metro")
}

dependencies {
    implementations(
        projects.core.common,
        projects.core.data.api,
        projects.core.data.impl,
        projects.core.datastore.api,
        projects.core.datastore.impl,
        projects.core.designsystem,
        projects.core.di,
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
