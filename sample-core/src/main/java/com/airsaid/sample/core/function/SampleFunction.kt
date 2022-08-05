package com.airsaid.sample.core.function

import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.SampleItem

/**
 * @author JackChen & Airsaid
 */
interface SampleFunction {
  /**
   * When open a new activity. This is the chance to setup somethings.
   *
   * @param context The context of the activity.
   */
  fun onInitialize(context: AppCompatActivity)

  /**
   * Check if this function is still available.
   *
   * @param clazz Whether the Class object is available.
   * @return true function will run or this function will passed.
   */
  fun isAvailable(clazz: Class<*>): Boolean

  /**
   * Run the function.
   *
   * @param context The context of the activity.
   * @param item The item that the user clicked.
   */
  fun execute(context: AppCompatActivity, item: SampleItem)
}
