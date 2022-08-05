package com.airsaid.sample.core.initializer

import android.content.Context
import androidx.startup.Initializer
import com.airsaid.sample.core.AndroidSample
import java.util.Collections

/**
 * @author JackChen & Airsaid
 */
class AndroidSampleInitializer : Initializer<AndroidSample> {
  override fun create(context: Context): AndroidSample {
    AndroidSample.attachToContext(context)
    return AndroidSample
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> {
    return Collections.emptyList()
  }
}
