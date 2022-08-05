package com.airsaid.sample.plugin

import com.github.jackchen.gradle.test.toolkit.runner.GradleRunnerProvider
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class SampleGradleRunnerProvider : GradleRunnerProvider {
  companion object {
    private val sharedTestKitDir = File(".").resolve(".gradle").absoluteFile.also {
      if (!it.exists()) it.mkdir()
    }
  }

  override fun getGradleRunner(projectDir: File, gradleVersion: String): GradleRunner {
    return GradleRunner.create().withProjectDir(projectDir).withGradleVersion(gradleVersion)
      .withTestKitDir(sharedTestKitDir).forwardOutput()
  }
}
