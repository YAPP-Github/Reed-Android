package com.ninecraft.booket.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCompose(
    extension: CommonExtension<*, *, *, *, *, *>,
) {
    applyPlugins(
        Plugins.KOTLIN_COMPOSE,
    )

    extension.apply {
        dependencies {
            implementation(platform(libs.androidx.compose.bom))
            implementation(libs.bundles.androidx.compose)
            debugImplementation(libs.androidx.compose.ui.tooling)
        }
    }
}
