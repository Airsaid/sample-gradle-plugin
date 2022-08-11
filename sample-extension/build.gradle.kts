plugins {
  id("sample.android.library")
  id("com.vanniktech.maven.publish")
}

dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.android.material)
  implementation(libs.androidx.startup.runtime)
  implementation(libs.recyclerview.library)
  implementation(libs.colorpicker)
  implementation(libs.viewpager2)
  api(projects.sampleCore)
}
