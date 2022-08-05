package com.airsaid.sample.plugin

import com.airsaid.sample.api.ExtensionItem
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.plugin.extension.SampleExtension
import com.airsaid.sample.plugin.instrumentation.AndroidSampleTemplateCreator
import com.airsaid.sample.plugin.instrumentation.SampleAsmClassVisitorFactory
import com.airsaid.sample.plugin.instrumentation.SampleClassHandler
import com.airsaid.sample.plugin.task.MergeSourceFileAndDocTask
import com.airsaid.sample.plugin.util.isAndroidProject
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.TransformClassesWithAsmTask
import com.google.gson.Gson
import org.gradle.api.*
import org.gradle.configurationcache.extensions.capitalized
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
    val sampleExtension = project.extensions.create(EXTENSION_NAME, SampleExtension::class.java)
    configureTransformClass(project)
    createSampleConfigurationClassAfterTransform(project, sampleExtension)
    configureCollectSourceFileAndDocTask(project)
  }

  private fun configureTransformClass(project: Project) {
    val androidComponentsExtension =
      project.extensions.getByType(AndroidComponentsExtension::class.java)
    androidComponentsExtension.onVariants { variant ->
      variant.transformClassesWith(
        SampleAsmClassVisitorFactory::class.java,
        InstrumentationScope.PROJECT
      ) {}
      variant.setAsmFramesComputationMode(
        FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
      )
    }
  }

  private fun configureCollectSourceFileAndDocTask(project: Project) {
    project.afterEvaluate {
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

  private fun createSampleConfigurationClassAfterTransform(
    project: Project,
    sampleExtension: SampleExtension
  ) {
    project.tasks.withType(TransformClassesWithAsmTask::class.java) { transformTask ->
      transformTask.doLast(object : Action<Task> {
        override fun execute(t: Task) {
          val files = transformTask.outputs.files.files
          val sampleList = SampleClassHandler.getSampleList()
          val extensionList = SampleClassHandler.getExtensionList()
          if (files.isNotEmpty()) {
            val destFolder = files.first()
            createSampleConfigurationClass(destFolder, sampleList, extensionList)
          }
          if (sampleExtension.enableDebug.get()) {
            PathNodePrinter.printPathTree(sampleList)
          }
        }
      })
    }
  }

  private fun createSampleConfigurationClass(
    destFolder: File,
    sampleItemList: List<SampleItem>,
    extensionList: List<ExtensionItem>
  ) {
    val configurationJsonText =
      Gson().toJson(mapOf("samples" to sampleItemList, "extensions" to extensionList))
    AndroidSampleTemplateCreator.create(destFolder, configurationJsonText)
  }
}
