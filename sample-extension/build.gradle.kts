plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = Versions.App.COMPILE_SDK

  defaultConfig {
    resourcePrefix = "sample_"
    minSdk = Versions.App.MIN_SDK
    targetSdk = Versions.App.TARGET_SDK
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.android.material)
  implementation(libs.androidx.startup.runtime)
  implementation(libs.recyclerview.library)
  api(projects.sampleCore)
}
