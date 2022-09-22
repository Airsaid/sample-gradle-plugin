plugins {
  id("sample.android.library")
  id("com.vanniktech.maven.publish")
  id("sample.ktlint")
}

dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.android.material)
  implementation(libs.androidx.startup.runtime)
  implementation(libs.colorpicker)
  implementation(libs.viewpager2)
  api(projects.sampleCore)
}
