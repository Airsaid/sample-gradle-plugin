package com.airsaid.sample.extension.component.memory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Extension
import com.airsaid.sample.core.component.ComponentContainer
import com.airsaid.sample.extension.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @author JackChen
 */
@Extension
class SampleMemoryComponent : ComponentContainer {
  override fun isComponentAvailable(component: Any): Boolean {
    val sampleDocument = component.javaClass.getAnnotation(SampleMemory::class.java)
    return null != sampleDocument && sampleDocument.isEnable
  }

  override fun getComponentView(
    context: AppCompatActivity,
    component: Any,
    parentView: ViewGroup,
    view: View
  ): View {
    val layoutInflater = LayoutInflater.from(context)
    val createView = layoutInflater.inflate(R.layout.sample_memory_layout, parentView, false)
    val memoryView = createView.findViewById<View>(R.id.sampleMemoryView)
    val sampleMemoryContainer = createView.findViewById<ViewGroup>(R.id.sampleMemoryContainer)
    val sampleMemoryButton = createView.findViewById<FloatingActionButton>(R.id.sampleMemoryButton)
    sampleMemoryButton.isSelected = true
    sampleMemoryButton.setOnClickListener {
      sampleMemoryButton.isSelected = !sampleMemoryButton.isSelected
      memoryView.visibility = if (sampleMemoryButton.isSelected) View.VISIBLE else View.INVISIBLE
    }
    sampleMemoryContainer.addView(view)
    return createView
  }

  override fun onCreatedView(context: AppCompatActivity, instance: Any, componentView: View) {}
}
