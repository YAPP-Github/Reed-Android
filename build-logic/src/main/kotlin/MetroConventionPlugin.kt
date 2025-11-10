import com.ninecraft.booket.convention.Plugins
import com.ninecraft.booket.convention.applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class MetroConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                Plugins.METRO,
            )
        }
    }
}
