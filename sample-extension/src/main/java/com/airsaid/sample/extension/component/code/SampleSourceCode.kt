package com.airsaid.sample.extension.component.code

/**
 * @author airsaid
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class SampleSourceCode(
  /**
   * If you want to filter the source files by regex.
   */
  val regex: String = "",
)
