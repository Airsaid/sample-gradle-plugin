package com.airsaid.sample.core.appcompat

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * @author airsaid
 */
abstract class AbstractSampleActivity : AppCompatActivity() {

  companion object {
    private const val ANDROID_SUPPORT_FRAGMENTS = "android:support:fragments"
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    // Because if we restart this activity. It will re-create the fragment by FragmentManagerState
    outState.remove(ANDROID_SUPPORT_FRAGMENTS)
  }

  /**
   * If user want to have his own toolbar. we won't add the standard toolbar for sample.
   */
  protected fun hasToolBar(view: View): Boolean {
    if (Toolbar::class.java === view.javaClass) {
      return true
    } else if (view is ViewGroup) {
      for (i in 0 until view.childCount) {
        val childView = view.getChildAt(i)
        return hasToolBar(childView)
      }
    }
    return false
  }
}
