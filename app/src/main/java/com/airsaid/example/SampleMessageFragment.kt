package com.airsaid.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.message.SampleMessage

@Register(
  title = "SampleMessage",
  desc = "Use @SampleMessage to take the messages output by System.out " +
    "and display them on the page."
)
@SampleMessage
class SampleMessageFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_sample_message, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<Button>(R.id.print).setOnClickListener {
      println("${System.currentTimeMillis()}: Hello World!")
    }
  }
}
