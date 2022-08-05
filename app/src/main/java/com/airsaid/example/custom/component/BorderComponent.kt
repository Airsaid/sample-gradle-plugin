package com.airsaid.example.custom.component

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Extension
import com.airsaid.sample.core.component.ComponentContainer

/**
 * @author JackChen
 */
@Extension
@Suppress("unused") // Identify and collect class information at compile time with @Extension
class BorderComponent : ComponentContainer {
  /**
   * We check if this object has [SampleBorder] annotation.
   *
   * If this sample object doesn't have this annotation. It won't call the other functions.
   */
  override fun isComponentAvailable(component: Any): Boolean {
    val sampleBorder = component.javaClass.getAnnotation(SampleBorder::class.java)
    return null != sampleBorder && sampleBorder.isEnable
  }

  /**
   * This function is an critical function. It's move like a chain. Each component will call this function
   * And return a new view for the next.
   */
  override fun getComponentView(
    context: AppCompatActivity,
    component: Any,
    parentView: ViewGroup,
    view: View
  ): View {
    val borderLayout = BorderLayout(context)
    borderLayout.addView(view)
    return borderLayout
  }

  /**
   * After this component created a new view. This function will call automatically.
   *
   * The view is the one you created. You only have this chance to initialize
   * your code here or it will be changed by the other component.
   */
  override fun onCreatedView(context: AppCompatActivity, instance: Any, componentView: View) {}
}
