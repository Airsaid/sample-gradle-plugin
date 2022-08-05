package com.airsaid.sample.plugin.extension

import org.gradle.api.provider.Property

/**
 * Configuration object for [com.airsaid.sample.plugin.SamplePlugin].
 *
 * @author airsaid
 */
abstract class SampleExtension {

  /**
   * Whether to enable debug mode. default disabled.
   */
  abstract val enableDebug: Property<Boolean>

  init {
    enableDebug.convention(false)
  }
}
