package com.airsaid.sample.api

/**
 * A marker annotation, used in conjunction with the [Register] annotation.
 *
 * The annotation marked sample object will be displayed first,
 * for quick debugging of sample.
 *
 * @author airsaid
 * @see Register
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestCase
