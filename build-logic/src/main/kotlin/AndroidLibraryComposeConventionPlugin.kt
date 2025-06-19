import com.android.build.gradle.LibraryExtension
import com.ninecraft.booket.convention.Plugins
import com.ninecraft.booket.convention.applyPlugins
import com.ninecraft.booket.convention.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                Plugins.ANDROID_LIBRARY,
                Plugins.KOTLIN_COMPOSE,
            )

            extensions.configure<LibraryExtension> {
                configureCompose(this)
            }
        }
    }
}
