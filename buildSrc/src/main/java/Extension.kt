import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.jitpack() {
  maven("https://jitpack.io")
}

fun Project.isAndroidProject() =
  plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")
