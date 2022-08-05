package com.airsaid.sample.core.window

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

/**
 * @author JackChen
 */
interface WindowDelegate {

  /**
   * When activity/fragment want to have view.
   *
   * @param context The android context object.
   * @param obj The object that want to have view.
   * @param parentView The container that the view will be added to.
   * @param view The view that will be added to the container.
   * @param saveInstance If the view is saved in the savedInstanceState.
   */
  fun onCreateView(context: AppCompatActivity, obj: Any, parentView: ViewGroup, view: View, saveInstance: Bundle?): View
}
