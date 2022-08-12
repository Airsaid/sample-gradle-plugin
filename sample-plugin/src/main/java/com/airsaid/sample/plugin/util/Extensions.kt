package com.airsaid.sample.plugin.util

import com.airsaid.sample.plugin.SamplePlugin
import com.airsaid.sample.plugin.constant.Constants.ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME
import com.airsaid.sample.plugin.constant.Constants.ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME
import com.android.build.gradle.AppPlugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import java.util.Locale

/**
 * Returns the Android application plugin if it is applied to the project.
 */
fun Project.isAndroidProject() = project.plugins.hasPlugin(AppPlugin::class.java)

/**
 * Returns a copy of this string having its first letter titlecased using the rules of the default locale,
 * or the original string if it's empty or already starts with a title case letter.
 *
 * The title case of a character is usually the same as its upper case with several exceptions.
 * The particular list of characters with the special title case form depends on the underlying platform.
 */
fun String.capitalized(): String {
  return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

/**
 * Returns whether the current class name needs to be processed.
 */
fun String.isNeedProcessedClassName(): Boolean {
  return this.toFullyQualifiedName() == ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME.toFullyQualifiedName() ||
    this.toFullyQualifiedName() == ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME.toFullyQualifiedName()
}

/**
 * Returns fully qualified name of current name.
 */
fun String.toFullyQualifiedName() = this.replace("/", ".")

lateinit var logEnable: Property<Boolean>

fun debug(logCategory: String? = null, message: String, e: Throwable? = null) =
  log(LogLevel.DEBUG, logCategory, message, e)

fun lifecycle(logCategory: String? = null, message: String, e: Throwable? = null) =
  log(LogLevel.LIFECYCLE, logCategory, message, e)

fun info(logCategory: String? = null, message: String, e: Throwable? = null) =
  log(LogLevel.INFO, logCategory, message, e)

fun error(logCategory: String? = null, message: String, e: Throwable? = null) =
  log(LogLevel.ERROR, logCategory, message, e)

fun warn(logCategory: String? = null, message: String, e: Throwable? = null) =
  log(LogLevel.WARN, logCategory, message, e)

private fun log(level: LogLevel, logCategory: String?, message: String, e: Throwable?) {
  if (logEnable.get()) {
    val category = "sample-plugin ${logCategory.orEmpty()}".trim()
    val logger = Logging.getLogger(SamplePlugin::class.java)
    if (e != null && level != LogLevel.ERROR && !logger.isDebugEnabled) {
      logger.log(level, "[$category] $message. Run with --debug option to get more log output.")
    } else {
      logger.log(level, "[$category] $message", e)
    }
  }
}

fun Project.logCategory(): String = path + name.takeIf { ":$it" != path }.orEmpty()

fun Task.logCategory(): String = project.logCategory() + path
