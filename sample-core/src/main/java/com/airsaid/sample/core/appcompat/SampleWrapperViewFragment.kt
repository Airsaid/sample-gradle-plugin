package com.airsaid.sample.core.appcompat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author JackChen & Airsaid
 */
open class SampleWrapperViewFragment(
  private val wrapView: View
) : Fragment() {

  companion object {
    fun newFragment(view: View) = SampleWrapperViewFragment(view)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return wrapView
  }
}
