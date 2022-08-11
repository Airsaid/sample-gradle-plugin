plugins {
  id("sample.android.application")
  id("com.airsaid.sample")
}

android {
  defaultConfig {
    applicationId = "com.airsaid.sample"
    versionCode = 1
    versionName = "1.0.0"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.android.material)
  implementation(libs.androidx.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso)
  implementation(projects.sampleExtension)
}
