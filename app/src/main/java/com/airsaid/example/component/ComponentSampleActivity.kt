package com.airsaid.example.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.code.SampleSourceCode
import com.airsaid.sample.extension.component.memory.SampleMemory
import com.airsaid.sample.extension.component.message.SampleMessage
import com.airsaid.example.custom.component.SampleBorder
import com.airsaid.example.databinding.ActivityComponentSampleBinding

@SampleMemory
@SampleMessage
@SampleBorder
@SampleSourceCode
@Register(title = "基础组件演示", desc = "展示通过注解添加三种不同组件为示例添加不同的功能扩展")
class ComponentSampleActivity : AppCompatActivity() {
  private var index = 0
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityComponentSampleBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Annotation above support two different functions

    // @SampleMemory will add memory panel to the sample
    // @SampleMessage will add message output panel
    binding.testButton.setOnClickListener {
      // This message will show up in message panel automatically
      println("Message from ComponentSampleActivity:${index++}")
    }
  }
}
