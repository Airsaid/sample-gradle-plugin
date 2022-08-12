package com.airsaid.sample.api

/**
 * When there are multiple sample item under the path, then
 * the name of path is used to distinguish them.
 *
 * @author airsaid
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PathDescription(
  /**
   * The path to be described.
   *
   * If not specified, the generation will be done automatically
   * based on the package name of the current class.
   */
  val path: String = "",

  /**
   * The path title of the [path].
   *
   * When specified, the category name of the path will be displayed in the list.
   * If it is not specified, the name of the most recent [path] will be displayed.
   */
  val title: String = "",

  /**
   * The path description of the [path].
   *
   * When specified, the category desc of the path will be displayed in the list.
   * If it is not specified, it will not be displayed.
   */
  val desc: String = ""
)
