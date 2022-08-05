package com.airsaid.sample.extension.component.message

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.airsaid.sample.api.Extension
import com.airsaid.sample.core.component.ComponentContainer
import com.airsaid.sample.extension.R
import java.util.Observer

/**
 * @author JackChen
 */
@Extension
class SampleMessageComponent : ComponentContainer {
  private val workThread: WorkThread<String> = WorkThread<String>("sample_work_thread")
  private val sampleSystemConsole = SampleSystemConsole()

  init {
    workThread.startService()
    sampleSystemConsole.setup(workThread)
  }

  override fun isComponentAvailable(component: Any): Boolean {
    val sampleMessage = component.javaClass.getAnnotation(SampleMessage::class.java)
    return null != sampleMessage && sampleMessage.value
  }

  override fun getComponentView(context: AppCompatActivity, component: Any, parentView: ViewGroup, view: View): View {
    val layoutInflater = LayoutInflater.from(context)
    val contentLayout = layoutInflater.inflate(R.layout.sample_message_layout, parentView, false)
    val sampleMessageContentLayout = contentLayout.findViewById<FrameLayout>(R.id.sampleMessageContentLayout)
    sampleMessageContentLayout.addView(view)
    return contentLayout
  }

  override fun onCreatedView(context: AppCompatActivity, instance: Any, componentView: View) {
    // If activity/fragment want to output message
    val scrollView = componentView.findViewById<NestedScrollView>(R.id.sampleScrollView)
    val messageView = componentView.findViewById<TextView>(R.id.sampleMessageText)
    val observer = Observer { _, o -> messageView.post { messageView.append(o.toString()) } }
    val scrollDownButton = componentView.findViewById<View>(R.id.scrollDownButton)
    scrollDownButton.isSelected = true
    val textWatcher: TextWatcher = object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
      override fun afterTextChanged(editable: Editable) {}
      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        if (scrollDownButton.isSelected) {
          scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
      }
    }
    messageView.addTextChangedListener(textWatcher)
    scrollDownButton.setOnClickListener {
      scrollDownButton.isSelected = !scrollDownButton.isSelected
      if (scrollDownButton.isSelected) {
        messageView.addTextChangedListener(textWatcher)
      } else {
        messageView.removeTextChangedListener(textWatcher)
      }
    }
    val clearMessageButton = componentView.findViewById<View>(R.id.clearMessageButton)
    clearMessageButton.setOnClickListener { messageView.text = null }
    workThread.addObserver(observer)
    // here we try to bind SampleMessageBindFragment
    SampleMessageBindFragment.injectIfNeededIn(context, workThread)
  }
}
