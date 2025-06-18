import com.ninecraft.booket.convention.applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "booket-android-library",
                "booket-android-hilt",
                "booket-android-library-compose",
            )

            dependencies {

            }
        }
    }
}
