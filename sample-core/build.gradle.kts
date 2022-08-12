plugins {
  id("sample.android.library")
  id("com.vanniktech.maven.publish")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("sample.ktlint")
}

android {
  buildFeatures {
    viewBinding = true
  }
}

dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.startup.runtime)
  implementation(libs.kotlin.serialization)
  api(projects.sampleApi)
}
