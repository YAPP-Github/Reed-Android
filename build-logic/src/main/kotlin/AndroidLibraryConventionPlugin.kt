import com.android.build.gradle.LibraryExtension
import com.ninecraft.booket.convention.Plugins
import com.ninecraft.booket.convention.applyPlugins
import com.ninecraft.booket.convention.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import com.ninecraft.booket.convention.ApplicationConstants

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                Plugins.ANDROID_LIBRARY,
                Plugins.KOTLIN_ANDROID,
            )

            extensions.configure<LibraryExtension> {
                configureAndroid(this)

                defaultConfig.apply {
                    targetSdk = ApplicationConstants.TARGET_SDK
                }
            }
        }
    }
}
