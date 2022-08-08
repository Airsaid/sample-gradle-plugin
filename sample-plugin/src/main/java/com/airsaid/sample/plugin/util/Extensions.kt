package com.airsaid.sample.plugin.util

import com.android.build.gradle.AppPlugin
import org.gradle.api.Project
import java.util.Locale

/**
 * Returns the Android application plugin if it is applied to the project.
 *
 * @return true if the Android application plugin is applied to the project.
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
