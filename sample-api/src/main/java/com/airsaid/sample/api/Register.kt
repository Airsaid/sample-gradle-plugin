package com.airsaid.sample.api

/**
 * Marks the specified class as a sample object.
 *
 * By default, subclasses of Activity or Fragment or DialogFragment
 * are supported as sample objects. Or, you can create new class
 * to add new support. The new class need to use the [Extension]
 * annotation and implement from the ActionProcessor interface.
 *
 * @author airsaid
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Register(
  /**
   * The title of the sample object.
   *
   * If not specified, the simple class name of the sample class
   * will be used by default.
   */
  val title: String = "",

  /**
   * The description of the sample object.
   *
   * If not specified, the description will not showed.
   */
  val desc: String = "",

  /**
   * The path of the sample object.
   *
   * If not specified, the generation will be done automatically
   * based on the package name of the simple class.
   */
  val path: String = "",
)
