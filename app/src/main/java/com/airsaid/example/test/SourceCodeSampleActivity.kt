package com.airsaid.example.test

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.example.databinding.ActivitySourceCodeSampleBinding
import com.airsaid.sample.api.Register

@Register(title = "演示源码加载", desc = "演示源码加载,高度展示")
class SourceCodeSampleActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivitySourceCodeSampleBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.sampleProgressBar.startProgressAnim()
    binding.sampleProgressBar.setOnProgressListener { v -> v.animate().alpha(0f) }
    binding.sourceCodeView.webChromeClient = object : WebChromeClient() {
      override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        if (newProgress >= binding.sampleProgressBar.firstProgress) {
          binding.sampleProgressBar.passAnimation()
        }
      }
    }
    binding.sourceCodeView.loadSourceCodeFromAssets(this, "MutableListAdapter.kt")
  }
}
