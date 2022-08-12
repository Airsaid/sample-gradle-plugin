package com.airsaid.sample.plugin

import com.airsaid.sample.api.SampleData
import com.airsaid.sample.plugin.extension.SampleExtension
import com.airsaid.sample.plugin.task.MergeSourceFileAndDocTask
import com.airsaid.sample.plugin.transform.SampleAsmClassVisitorFactory
import com.airsaid.sample.plugin.transform.processor.SampleProcessor
import com.airsaid.sample.plugin.util.SampleConfigClassCreator
import com.airsaid.sample.plugin.util.capitalized
import com.airsaid.sample.plugin.util.isAndroidProject
import com.airsaid.sample.plugin.util.lifecycle
import com.airsaid.sample.plugin.util.logCategory
import com.airsaid.sample.plugin.util.logEnable
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.TransformClassesWithAsmTask
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File

/**
 * @author JackChen
 */
class SamplePlugin : Plugin<Project> {

  companion object {
    private const val EXTENSION_NAME = "sample"
  }

  override fun apply(project: Project) {
    if (!project.isAndroidProject()) {
      throw GradleException("${project.name} is not an Android project.")
    }

    project.initExtensions()
    project.configureTransformClass()
    project.createSampleConfigurationClassAfterTransform()
    project.configureCollectSourceFileAndDocTask()
  }

  private fun Project.initExtensions() {
    val sampleExtension = extensions.create(EXTENSION_NAME, SampleExtension::class.java)
    logEnable = sampleExtension.enableDebug
  }

  private fun Project.configureTransformClass() {
    val androidComponentsExtension = extensions.getByType(AndroidComponentsExtension::class.java)
    androidComponentsExtension.onVariants { variant ->
      variant.transformClassesWith(
        SampleAsmClassVisitorFactory::class.java,
        InstrumentationScope.PROJECT
      ) {}
      variant.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
    }
  }

  private fun Project.createSampleConfigurationClassAfterTransform() {
    tasks.withType(TransformClassesWithAsmTask::class.java) { transformTask ->
      transformTask.doLast(object : Action<Task> {
        override fun execute(t: Task) {
          val files = transformTask.outputs.files.files
          if (files.isNotEmpty()) {
            val destFolder = files.first()
            createSampleConfigurationClass(destFolder)
          }
        }
      })
    }
  }

  private fun Project.configureCollectSourceFileAndDocTask() {
    afterEvaluate {
      val appExtension = project.extensions.getByType(AppExtension::class.java)
      appExtension.applicationVariants.forEach { applicationVariant ->
        val task = project.tasks.create(
          MergeSourceFileAndDocTask.TASK_NAME + "With" +
            applicationVariant.flavorName.capitalized() +
            applicationVariant.buildType.name.capitalized(),
          MergeSourceFileAndDocTask::class.java
        )
        if (applicationVariant.mergeAssetsProvider.isPresent) {
          val mergeAssetsTask = applicationVariant.mergeAssetsProvider.get()
          mergeAssetsTask.dependsOn(task)
        }
      }
    }
  }

  private fun Project.createSampleConfigurationClass(destFolder: File) {
    val sampleItems = SampleProcessor.getSampleItems()
    val extensionItems = SampleProcessor.getExtensionItems()
    val pathItems = SampleProcessor.getPathItems()
    val sampleData = SampleData(sampleItems, extensionItems, pathItems)
    val sampleConfigJson = Json.encodeToString(sampleData)
    lifecycle(project.logCategory(), "sampleConfigJson: $sampleConfigJson")
    SampleConfigClassCreator.create(destFolder, sampleConfigJson)
  }
}
