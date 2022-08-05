package com.airsaid.example

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.code.SampleSourceCode
import com.airsaid.sample.extension.component.document.SampleDocument
import com.airsaid.sample.extension.component.memory.SampleMemory
import com.airsaid.sample.extension.component.message.SampleMessage
import com.airsaid.sample.extension.permission.SamplePermission

@SampleMemory
@SampleMessage
@SampleSourceCode("SampleListFragment.kt")
@SampleDocument("assets://sample_document.md")
@SamplePermission(Manifest.permission.READ_EXTERNAL_STORAGE)
@Register(
  title = "SampleList",
  desc = "Multiple sample annotations can be used together."
)
class SampleListFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_sample_text, container, false)
  }

  @SuppressLint("SetTextI18n")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<TextView>(R.id.textView).apply {
      text = "Multiple sample annotations can be used together."
    }
  }
}
