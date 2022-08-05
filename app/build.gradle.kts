plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.airsaid.sample")
}

android {
  compileSdk = Versions.App.COMPILE_SDK

  defaultConfig {
    applicationId = "com.airsaid.sample"
    minSdk = Versions.App.MIN_SDK
    targetSdk = Versions.App.TARGET_SDK
    versionCode = Versions.App.VERSION_CODE
    versionName = Versions.App.VERSION_NAME

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

  kotlinOptions {
    jvmTarget = "1.8"
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
