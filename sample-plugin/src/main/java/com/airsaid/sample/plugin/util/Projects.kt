package com.airsaid.sample.plugin.util

import com.android.build.gradle.AppPlugin
import org.gradle.api.Project

/**
 * Returns the Android application plugin if it is applied to the project.
 *
 * @return true if the Android application plugin is applied to the project.
 */
fun Project.isAndroidProject() = project.plugins.hasPlugin(AppPlugin::class.java)
