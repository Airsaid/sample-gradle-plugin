package com.airsaid.sample.plugin

import com.github.jackchen.gradle.test.toolkit.GradlePluginTest
import com.github.jackchen.gradle.test.toolkit.ext.TestVersion
import com.github.jackchen.gradle.test.toolkit.ext.TestWithCache
import com.github.jackchen.gradle.test.toolkit.testdsl.TestProjectIntegration
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 * @author JackChen
 */
class SamplePluginTest : GradlePluginTest() {

  private fun testProjectSetup(
    isEnableDebug: Boolean = false,
    closure: TestProjectIntegration.TestProject.() -> Unit
  ) {
    val pluginVersion = System.getProperty("com.airsaid.sample.version")
    val testPackageName = "com.airsaid.sample.plugin.test"
    kotlinAndroidTemplate {
      template {
        `package` {
          packageName = testPackageName
        }
        plugins {
          id("com.airsaid.sample").version(pluginVersion)
        }
        dependencies {
          implementation("androidx.core:core-ktx:1.7.0")
          implementation("androidx.appcompat:appcompat:1.4.1")
          implementation("com.airsaid:sample-api:$pluginVersion")
        }
      }
      project {
        module("app") {
          file("build.gradle.kts") {
            """
                            plugins {
                            	id("com.android.application")
                            	id("org.jetbrains.kotlin.android")
                            	id("com.airsaid.sample")
                            }
                            sample {
                               enableDebug.set($isEnableDebug)
                            }
                            android {
                                compileSdk = 31
                                defaultConfig {
                                    applicationId = "$testPackageName"
                                    minSdk = 21
                                    targetSdk = 31
                                    versionCode = 1
                                    versionName = "1.0"
                                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
                            	implementation("androidx.core:core-ktx:1.7.0")
                            	implementation("androidx.appcompat:appcompat:1.4.1")
                             implementation("com.airsaid:sample-api:$pluginVersion")
                            }
                        """.trimIndent()
          }
          sourceDir(testPackageName) {
            file("test.md") {
              """
                                ### Readme
                                This is a test mark down file.
              """.trimIndent()
            }
            file("TestActivity.kt") {
              """
                            |package ${packageName()}
                            |@com.airsaid.sample.api.Register(title = "TestActivity")
                            |@com.airsaid.sample.api.PathDescription(title = "Test Case", desc = "This is a test case.")
                            |class TestActivity : androidx.appcompat.app.AppCompatActivity()
                            """.trimMargin()
            }
            file("TestFragment.kt") {
              """
                            |package ${packageName()}
                            |@com.airsaid.sample.api.Register(title = "TestFragment", desc = "This is a test fragment.")
                            |class TestFragment : androidx.fragment.app.Fragment()
                            """.trimMargin()
            }
            file("TestDialog.kt") {
              """
                            |package $testPackageName
                            |@com.airsaid.sample.api.Register(title = "TestDialogFragment")
                            |class TestDialogFragment : androidx.fragment.app.DialogFragment()
                            """.trimMargin()
            }
          }
        }
        apply(closure)
      }
    }
  }

  @Test
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test sample plugin build`() {
    testProjectSetup {
      build(":app:transformDebugClassesWithAsm") {
        Assertions.assertEquals(
          TaskOutcome.SUCCESS,
          task(":app:transformDebugClassesWithAsm")?.outcome
        )
      }
    }
  }

  @Test
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test assemble build`() {
    // :app:assembleDebug
    testProjectSetup {
      build(":app:assembleDebug") {
        Assertions.assertEquals(TaskOutcome.SUCCESS, task(":app:assembleDebug")?.outcome)
      }
    }
  }

  @Test
  @TestVersion(androidVersion = "7.0.0", gradleVersion = "7.0.2")
  fun `test assemble build by lower version`() {
    testProjectSetup {
      build(":app:assembleDebug") {
        Assertions.assertEquals(TaskOutcome.SUCCESS, task(":app:assembleDebug")?.outcome)
      }
    }
  }

  @Test
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test debug enable extension`() {
    testProjectSetup(isEnableDebug = true) {
      build(":app:transformDebugClassesWithAsm") {
        Assertions.assertEquals(
          TaskOutcome.SUCCESS,
          task(":app:transformDebugClassesWithAsm")?.outcome
        )
        Assertions.assertTrue(output.contains("sampleConfigJson:"))
      }
    }
  }

  private fun buildTask(
    gradleRunner: GradleRunner,
    task: String,
    assertions: BuildResult.() -> Unit = {}
  ) {
    val buildArgumentList = mutableListOf<String>()
    buildArgumentList.add(task)
//    buildArgumentList.add("--info")
    gradleRunner
      .withArguments(buildArgumentList)
      .withDebug(true)
      .build()
      .run { assertions() }
  }

  @Test
  @TestWithCache(true)
  @TestVersion(androidVersion = "7.2.1", gradleVersion = "7.4.1")
  fun `test plugin with incremental`() {
    testProjectSetup(isEnableDebug = true) {
      val gradleRunnerProvider = SampleGradleRunnerProvider()
      val gradleRunner = gradleRunnerProvider.getGradleRunner(
        testProjectRunner.projectDir,
        testProjectRunner.testVersions.supportedGradleVersion
      )
      module("app") {
        kotlinSourceDir("com.android.test") {
          // New file
          val testMethod = "testMethod${Random.nextInt().absoluteValue}"
          file("TestDialog2.kt") {
            """
              |package com.android.test
              |import androidx.appcompat.app.AppCompatDialog
              |@com.airsaid.sample.api.Register(title="The test dialog2")
              |class TestDialog2{
              |   fun $testMethod(){
              |     println("$testMethod")
              |   }
              |}
            """.trimMargin()
          }
        }
      }
      buildTask(gradleRunner, ":app:transformDebugClassesWithAsm") {
        Assertions.assertEquals(
          TaskOutcome.SUCCESS,
          task(":app:transformDebugClassesWithAsm")?.outcome
        )
      }
    }
  }
}
