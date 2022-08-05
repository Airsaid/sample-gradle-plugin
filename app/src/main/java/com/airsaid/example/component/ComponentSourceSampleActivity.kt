package com.airsaid.example.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.code.SampleSourceCode
import com.airsaid.example.R

@SampleSourceCode
@Register(title = "源码组件", desc = "展示如何为示例添加源码查看组件")
class ComponentSourceSampleActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_component_source_code_sample)
  }
}
