package com.airsaid.example.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.example.databinding.ActivityComponentListSampleBinding
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.code.SampleSourceCode
import com.airsaid.sample.extension.component.document.SampleDocument
import com.airsaid.sample.extension.component.memory.SampleMemory
import com.airsaid.sample.extension.component.message.SampleMessage

@SampleMemory
@SampleMessage
@SampleSourceCode
@SampleDocument("assets://documentSample.md")
@Register(title = "组件集", desc = "展示当前己扩展的组件")
class ComponentListSampleActivity : AppCompatActivity() {
  private var index = 0
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityComponentListSampleBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.testButton.setOnClickListener {
      // This message will show up in message panel automatically
      println("Message from ComponentListSampleActivity:${index++}\n")
    }
  }
}
