import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.gradle.dependency.handler.extensions)
    alias(libs.plugins.kotlin.detekt)
    alias(libs.plugins.kotlin.ktlint)
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

val excludeModules: String? by project

allprojects {
    if (excludeModules?.split(",")?.contains(project.name) != true) {
        apply {
            plugin(rootProject.libs.plugins.kotlin.detekt.get().pluginId)
            plugin(rootProject.libs.plugins.kotlin.ktlint.get().pluginId)
            plugin(rootProject.libs.plugins.gradle.dependency.handler.extensions.get().pluginId)
        }

        allprojects {
            configurations.all {
                resolutionStrategy {
                    // ktlint 관련 의존성 충돌 강제 해결
                    force("com.pinterest.ktlint:ktlint-rule-engine-core:0.50.0")
                    force("com.pinterest.ktlint:ktlint-ruleset-standard:0.50.0")
                    force("com.pinterest.ktlint:ktlint-cli-ruleset-core:0.50.0")
                    force("com.pinterest.ktlint:ktlint-logger:0.50.0")

                    // 또는 모든 ktlint를 exclusion
                    exclude(group = "com.pinterest.ktlint")
                }
            }
        }

        afterEvaluate {
            extensions.configure<DetektExtension> {
                parallel = true
                buildUponDefaultConfig = true
                toolVersion = libs.versions.kotlin.detekt.get()
                config.setFrom(files("$rootDir/detekt-config.yml"))
            }

            extensions.configure<KtlintExtension> {
                version.set(rootProject.libs.versions.kotlin.ktlint.source.get())
                android.set(true)
                verbose.set(true)
            }
        }
    }
}
