package com.airsaid.sample.core.main

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.core.AndroidSample

/**
 * @author JackChen
 */
class SampleActivityLifeCycleCallback : ActivityLifecycleCallbacks {
  override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    // initialize all functions
    val functionManager = AndroidSample.getFunctionManager()
    val functionList = functionManager.getFunctionList()
    for (function in functionList) {
      if (activity is AppCompatActivity) {
        function.onInitialize(activity)
      }
    }
  }

  override fun onActivityStarted(activity: Activity) {}
  override fun onActivityResumed(activity: Activity) {}
  override fun onActivityPaused(activity: Activity) {}
  override fun onActivityStopped(activity: Activity) {}
  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
  override fun onActivityDestroyed(activity: Activity) {}
}
