package com.airsaid.sample.core.processor

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.appcompat.SampleFragmentContainerActivity
import com.airsaid.sample.core.exception.SampleFailedException

/**
 * @author JackChen
 */
class FragmentClassActionProcessor : ActionProcessor {

  override fun isAvailable(clazz: Class<*>): Boolean {
    return Fragment::class.java.isAssignableFrom(clazz)
  }

  @Throws(SampleFailedException::class)
  override fun execute(context: AppCompatActivity, sampleItem: SampleItem) {
    SampleFragmentContainerActivity.startActivity(context, sampleItem)
  }
}
