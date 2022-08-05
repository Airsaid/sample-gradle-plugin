package com.airsaid.sample.core.processor

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.SampleConstants

/**
 * @author JackChen
 */
class ActivityClassActionProcessor : ActionProcessor {

  override fun isAvailable(clazz: Class<*>): Boolean {
    return Activity::class.java.isAssignableFrom(clazz)
  }

  override fun execute(context: AppCompatActivity, sampleItem: SampleItem) {
    Intent(context, sampleItem.clazz()).apply {
      putExtra(SampleConstants.PARAMETER_TITLE, sampleItem.title)
      putExtra(SampleConstants.PARAMETER_DESC, sampleItem.desc)
      context.startActivity(this)
    }
  }
}
