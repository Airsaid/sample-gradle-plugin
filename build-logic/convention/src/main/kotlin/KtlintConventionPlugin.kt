
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class KtlintConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("org.jlleitschuh.gradle.ktlint")
      }

      val isAndroidProject = plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")
      logger.lifecycle("isAndroidProject: $isAndroidProject")
      extensions.configure<KtlintExtension> {
        debug.set(true)
        android.set(isAndroidProject)
        additionalEditorconfigFile.set(file("$rootDir/.editorconfig"))
      }
    }
  }
}
