package com.airsaid.sample.core.window

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.core.component.ComponentManager.getComponentContainerSet

/**
 * @author JackChen
 */
class AppcompatWindowDelegate : WindowDelegate {

  override fun onCreateView(
    context: AppCompatActivity,
    obj: Any,
    parentView: ViewGroup,
    view: View,
    saveInstance: Bundle?
  ): View {
    val componentContainerSet = getComponentContainerSet()
    var componentView = view
    for (componentContainer in componentContainerSet) {
      // If this component is available
      if (componentContainer.isComponentAvailable(obj)) {
        // We use different component change the view
        componentView = componentContainer.getComponentView(context, obj, parentView, componentView)
        // Here we call onCreateView function
        componentContainer.onCreatedView(context, componentContainer, componentView)
      }
    }
    return componentView
  }
}
