package com.airsaid.example.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airsaid.example.databinding.FragmentComponentSampleBinding
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.code.SampleSourceCode
import com.airsaid.sample.extension.component.document.SampleDocument
import com.airsaid.sample.extension.component.memory.SampleMemory
import com.airsaid.sample.extension.component.message.SampleMessage

@SampleMemory
@SampleMessage
@SampleSourceCode
@SampleDocument("assets://documentSample.md")
@Register(title = "Fragment显示组件", desc = "演示在 Fragment 中展示所有基础组件")
class ComponentSampleFragment : Fragment() {
  private lateinit var binding: FragmentComponentSampleBinding
  private var index = 0
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = FragmentComponentSampleBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    binding.testButton.setOnClickListener {
      println("Message from ComponentSampleActivity:${index++}")
    }
  }
}
