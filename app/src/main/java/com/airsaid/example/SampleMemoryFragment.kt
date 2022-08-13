package com.airsaid.example

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.memory.SampleMemory

@Register(
  title = "SampleMemory",
  desc = "Use @SampleMemory to quickly see the current memory usage on the page."
)
@SampleMemory
class SampleMemoryFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_sample_memory, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<Button>(R.id.load).setOnClickListener {
      BitmapFactory.decodeResource(requireContext().resources, R.mipmap.ic_launcher)
    }
  }
}
