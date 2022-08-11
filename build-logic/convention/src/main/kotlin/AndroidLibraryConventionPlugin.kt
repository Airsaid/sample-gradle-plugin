import com.android.build.gradle.LibraryExtension
import com.airsaid.sample.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
      }

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        with(defaultConfig) {
          targetSdk = 31
          resourcePrefix = "sample_"
        }
      }
    }
  }
}
