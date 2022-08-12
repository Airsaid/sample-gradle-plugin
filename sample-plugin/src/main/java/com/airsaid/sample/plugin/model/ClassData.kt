package com.airsaid.sample.plugin.model

/**
 * @author airsaid
 */
data class ClassData(
  /**
   * Fully qualified name of the class.
   */
  val className: String,

  /**
   * Fully qualified name of the super class.
   */
  val superClassName: String,

  /**
   * Map of the annotations the class has.
   *
   * The key is name of annotation, the value is parameters of annotation.
   */
  val classAnnotations: Map<String, List<AnnotationParam>> = emptyMap(),

  /**
   * List of all the interfaces that this class or a superclass of this class implements.
   */
  val interfaces: List<String> = emptyList(),
) {
  data class AnnotationParam(
    /**
     * Parameter name of the annotation.
     */
    val name: String,

    /**
     * Parameter value of the annotation.
     */
    val value: Any?
  )
}
