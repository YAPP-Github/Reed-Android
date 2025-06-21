import com.android.build.gradle.LibraryExtension
import com.ninecraft.booket.convention.ApplicationConstants
import com.ninecraft.booket.convention.Plugins
import com.ninecraft.booket.convention.applyPlugins
import com.ninecraft.booket.convention.configureAndroid
import com.ninecraft.booket.convention.implementation
import com.ninecraft.booket.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidRetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "booket.kotlin.library.serialization",
            )

            extensions.configure<LibraryExtension> {
                configureAndroid(this)

                defaultConfig.apply {
                    targetSdk = ApplicationConstants.TARGET_SDK
                }
            }

            dependencies {
                implementation(libs.retrofit)
                implementation(libs.retrofit.kotlinx.serialization.converter)
                implementation(libs.okhttp.logging.interceptor)
            }
        }
    }
}
