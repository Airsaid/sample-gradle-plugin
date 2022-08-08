package com.airsaid.sample.extension.permission

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.airsaid.sample.api.Extension
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.function.SampleFunction

@Extension
class SamplePermissionFunction : SampleFunction {
  /**
   * If your function wants to do some initial work. Here we inject the fragment.
   * But if we don't we this function, call SamplePermissionsFragment.injectIfNeededIn(context);
   * and then try to get fragment from the FragmentManager It just didn't exist
   * @param context
   */
  override fun onInitialize(context: AppCompatActivity) {
    // inject permission fragment
    SamplePermissionsFragment.injectIfNeededIn(context)
  }

  /**
   * Check this class and determined this class needs to run this function
   * @param clazz
   * @return
   */
  override fun isAvailable(clazz: Class<*>): Boolean {
    val samplePermission = clazz.getAnnotation(SamplePermission::class.java)
    return samplePermission?.value != null && samplePermission.value.isNotEmpty()
  }

  override fun execute(context: AppCompatActivity, item: SampleItem) {
    context.application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        context.application.unregisterActivityLifecycleCallbacks(this)
        if (activity is AppCompatActivity) {
          requestSamplePermission(activity, item)
        }
      }

      override fun onActivityStarted(activity: Activity) {}
      override fun onActivityResumed(activity: Activity) {}
      override fun onActivityPaused(activity: Activity) {}
      override fun onActivityStopped(activity: Activity) {}
      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
      override fun onActivityDestroyed(activity: Activity) {}
    })
  }

  private fun requestSamplePermission(activity: FragmentActivity, item: SampleItem) {
    val clazz = item.clazz()
    val samplePermission = clazz.getAnnotation(SamplePermission::class.java)
    // Here add permission observer
    val permissionsFragment = SamplePermissionsFragment.get(activity)
    // Request permission
    permissionsFragment.requestPermissions(samplePermission!!.value)
  }
}
