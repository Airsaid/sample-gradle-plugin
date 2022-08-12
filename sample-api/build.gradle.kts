plugins {
  id("org.gradle.java-library")
  id("org.jetbrains.kotlin.jvm")
  id("com.vanniktech.maven.publish")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("sample.ktlint")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  compileOnly(libs.androidx.annotation)
  compileOnly(libs.kotlin.serialization)
}
