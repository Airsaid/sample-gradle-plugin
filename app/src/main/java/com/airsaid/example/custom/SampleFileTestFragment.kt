package com.airsaid.example.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.component.code.SampleSourceCode
import com.airsaid.example.R

@SampleSourceCode
@Register(title = "Source test", desc = "查看多层级的源码目录")
class SampleFileTestFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_source_test_sample, container, false)
  }
}
