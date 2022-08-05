package com.airsaid.sample.core.component

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

/**
 * This interface can dynamically extend the view to the sample page.
 *
 * @author JackChen
 */
interface ComponentContainer {
  /**
   * Returns whether the given class object is the class that needs to be processed.
   *
   * @param component The instance of the sample. It depends on which one that you registered.
   * @return true if the class object is the class needs to be processed.
   * Otherwise, false is returned, and subsequent methods are not executed.
   */
  fun isComponentAvailable(component: Any): Boolean

  /**
   * This function is an critical function. It's move like a chain. Each component will call this function
   * And return a new view for the next.
   *
   * @param context The current activity.
   * @param component The instance of the sample. It depends on which one that you registered.
   * @param parentView The parent view of your original view.
   * @param view The fragment/activity content view.
   * @return The new view that you want to add to the sample page.
   */
  fun getComponentView(context: AppCompatActivity, component: Any, parentView: ViewGroup, view: View): View

  /**
   * After this component created a new view. This function will call automatically.
   * The view is the one you created. You only have this chance to initialize your code
   * here or it will be changed by the other component.
   *
   * @param context The current activity.
   * @param instance An instance object that implements the current interface.
   * @param componentView The view object returned by the [getComponentView] method.
   */
  fun onCreatedView(context: AppCompatActivity, instance: Any, componentView: View)

  /**
   * Returns the priority of the current component in the queue. If you want your component run before others.
   * The larger the priority, the higher the priority.
   */
  fun getComponentPriority(): Int = 0
}
