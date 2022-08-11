plugins {
  `kotlin-dsl`
}

group = "com.airsaid.sample.buildlogic"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  implementation(libs.android.gradle.plugin)
  implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "sample.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidLibrary") {
      id = "sample.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
  }
}
