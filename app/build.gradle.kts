@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.booket.android.application)
    alias(libs.plugins.booket.android.application.compose)
    alias(libs.plugins.booket.android.hilt)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

android {
    namespace = "com.ninecraft.booket"

    defaultConfig {
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getApiKey("KAKAO_NATIVE_APP_KEY"))
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = (localProperties["KAKAO_NATIVE_APP_KEY"] as String).trim('"')
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
        projects.core.designsystem,

        projects.feature.home,
        projects.feature.library,
        projects.feature.main,
        projects.feature.search,

        libs.androidx.activity.compose,
        libs.androidx.startup,
        libs.logger,
        libs.kakao.auth,

        libs.bundles.circuit,
    )
    api(libs.circuit.codegen.annotation)
    ksp(libs.circuit.codegen.ksp)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
