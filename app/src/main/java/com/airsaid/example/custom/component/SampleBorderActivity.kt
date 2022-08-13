package com.airsaid.example.custom.component

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.example.R
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.code.SampleSourceCode

@Register(
  title = "SampleBorder",
  desc = "Demonstrate how to dynamically extension component."
)
@SampleBorder
@SampleSourceCode
class SampleBorderActivity : AppCompatActivity() {

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_sample_text)
    findViewById<TextView>(R.id.textView).apply {
      text = "@SampleBorder is a custom extended component that adds a border display to the sample view."
    }
  }
}
