package com.airsaid.sample.api

/**
 * Marks the specified class as an extended object.
 *
 * The extension class must implement one of the SampleFunction or
 * ComponentContainer or ActionProcessor interfaces as needed.
 *
 * @author airsaid
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Extension
