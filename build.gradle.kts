// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
  alias(libs.plugins.android.application).apply(false)
  alias(libs.plugins.android.library).apply(false)
  alias(libs.plugins.kotlin.android).apply(false)
  alias(libs.plugins.kotlin.serialization).apply(false)
  alias(libs.plugins.vanniktech.maven.publish).apply(false)
  alias(libs.plugins.sample).apply(false)
  alias(libs.plugins.ktlint).apply(true)
}

subprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")
  afterEvaluate {
    val isAndroidProject = plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")
    ktlint {
      debug.set(true)
      android.set(isAndroidProject)
      additionalEditorconfigFile.set(file("$rootDir/.editorconfig"))
    }
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
