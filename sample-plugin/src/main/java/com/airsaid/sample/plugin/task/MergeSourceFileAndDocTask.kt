package com.airsaid.sample.plugin.task

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet
import com.android.build.gradle.internal.api.DefaultAndroidSourceSet
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

/**
 * @author JackChen
 */
open class MergeSourceFileAndDocTask : DefaultTask() {
  companion object {
    const val TASK_NAME = "mergeSourceFileAndDoc"
    private const val SAMPLE_DIR_NAME = "sample"
    private const val SAMPLE_DOCUMENT_FOLDER = "document"
  }

  @TaskAction
  fun collectSourceFileAndDoc() {
    val sampleBuildDir = prepareSampleDirectory()
    // Merge all the documents inside the project.
    val documentBuildFile = File(sampleBuildDir, SAMPLE_DOCUMENT_FOLDER)
    mergeDocumentFiles(project, documentBuildFile)
    // We copy all the source file into the assets folder.
    val appExtension = project.extensions.getByType(AppExtension::class.java)
    appExtension.sourceSets.forEach { sourceSet ->
      if (sourceSet.name == "main") {
        if (sourceSet is DefaultAndroidSourceSet) {
          val javaSourceSet = sourceSet.java
          if (javaSourceSet is DefaultAndroidSourceDirectorySet) {
            sourceSet.assets.srcDirs(javaSourceSet.srcDirs)
          }
          val kotlinSourceSet = sourceSet.kotlin
          if (kotlinSourceSet is DefaultAndroidSourceDirectorySet) {
            sourceSet.assets.srcDirs(kotlinSourceSet.srcDirs)
          }
          sourceSet.assets.srcDirs(sampleBuildDir.absolutePath)
        }
      }
    }
  }

  private fun prepareSampleDirectory(): File {
    val sampleBuildDir = File(project.buildDir, SAMPLE_DIR_NAME)
    if (sampleBuildDir.exists()) {
      sampleBuildDir.deleteRecursively()
    }
    if (!sampleBuildDir.exists()) {
      sampleBuildDir.mkdirs()
    }
    return sampleBuildDir
  }

  /**
   * Collect all the documents inside this project.
   */
  private fun mergeDocumentFiles(project: Project, outputFolder: File) {
    val projectDir = project.rootProject.projectDir
    val projectAbsolutePath = projectDir.absolutePath
    val documentList = collectDocumentFiles(project)
    for (file in documentList) {
      try {
        val fileRelativePath = file.absolutePath.substring(projectAbsolutePath.length + 1)
        val destFile = File(outputFolder, fileRelativePath)
        val destFolder = destFile.parentFile
        if (!destFolder.exists()) {
          destFolder.mkdir()
        }
        FileUtils.copyFile(file, destFile)
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  private fun collectDocumentFiles(project: Project): List<File> {
    val documentList = mutableListOf<File>()
    val ignoreFiles = hashSetOf<String>()
    ignoreFiles.add(".idea")
    ignoreFiles.add(".gradle")
    ignoreFiles.add("build")
    try {
      val projectDir = project.rootProject.projectDir
      Files.walkFileTree(
        projectDir.toPath(),
        object : SimpleFileVisitor<Path>() {
          @Throws(IOException::class)
          override fun preVisitDirectory(
            path: Path,
            attributes: BasicFileAttributes
          ): FileVisitResult {
            val name = path.toFile().name
            return if (ignoreFiles.contains(name)) {
              FileVisitResult.SKIP_SUBTREE
            } else {
              FileVisitResult.CONTINUE
            }
          }

          override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
            if (!attrs.isDirectory) {
              val f = file.toFile()
              if (f.isDocumentFile()) {
                documentList.add(f)
              }
            }
            return FileVisitResult.CONTINUE
          }
        }
      )
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return documentList
  }

  private fun File.isDocumentFile() = this.name.endsWith(".MD", true)
}
