package com.airsaid.sample.extension.permission

/**
 * @author JackChen
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class SamplePermission(vararg val value: String)
