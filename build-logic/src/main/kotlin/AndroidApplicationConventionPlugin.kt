import com.android.build.api.dsl.ApplicationExtension
import com.ninecraft.booket.convention.ApplicationConstants
import com.ninecraft.booket.convention.Plugins
import com.ninecraft.booket.convention.applyPlugins
import com.ninecraft.booket.convention.configureAndroid
import com.ninecraft.booket.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                Plugins.ANDROID_APPLICATION,
                Plugins.KOTLIN_ANDROID,
            )

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)

                defaultConfig {
                    targetSdk = libs.versions.targetSdk.get().toInt()
                    versionName = libs.versions.versionName.get()
                    versionCode = libs.versions.versionCode.get().toInt()
                }
            }
        }
    }
}
