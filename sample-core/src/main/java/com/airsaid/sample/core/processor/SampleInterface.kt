package com.airsaid.sample.core.processor

import androidx.appcompat.app.AppCompatActivity

/**
 * @author JackChen
 */
interface SampleInterface<T> {

  /**
   * Returns the object when you want to display.
   *
   * @return The object when you want to display.
   */
  fun getObject(context: AppCompatActivity): T
}
