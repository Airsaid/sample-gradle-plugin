package com.airsaid.example.custom.function

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Extension
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.function.SampleFunction

/**
 * This is an example of an extension implementation from the [SampleFunction] interface
 * that outputs information to the logcat when a item is clicked.
 *
 * @author airsaid
 */
@Extension
class PrintSampleFunction : SampleFunction {
  companion object {
    private const val TAG = "PrintSampleFunction"
  }

  override fun onInitialize(context: AppCompatActivity) {
  }

  override fun isAvailable(clazz: Class<*>): Boolean {
    return true
  }

  override fun execute(context: AppCompatActivity, item: SampleItem) {
    Log.d(TAG, "execute context: $context, item: $item")
  }
}
