package com.airsaid.example.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.example.R
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.document.SampleDocument

@SampleDocument("assets://documentSample.md")
@Register(title = "文档组件", desc = "展示如何为演示关联文档")
class ComponentDocumentSampleActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_component_document_sample)
  }
}
