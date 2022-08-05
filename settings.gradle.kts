pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://plugins.gradle.org/m2/")
    maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://plugins.gradle.org/m2/")
    maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
  }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "sample-gradle-plugin"
include(":app", ":sample-api", ":sample-core", ":sample-extension", ":sample-plugin")
