package com.airsaid.sample.extension.initializer

import android.content.Context
import com.airsaid.sample.core.AndroidSample
import com.airsaid.sample.extension.component.SamplePagerComponentContainer
import com.airsaid.sample.extension.component.memory.SampleMemoryComponent
import com.airsaid.sample.extension.component.message.SampleMessageComponent
import com.airsaid.sample.extension.permission.SamplePermissionFunction

class AndroidSampleExtensions {

  fun initialize(context: Context) {
    val functionManager = AndroidSample.getFunctionManager()
    functionManager.register(SamplePermissionFunction())

    val componentManager = AndroidSample.getComponentManager()
    componentManager.register(SamplePagerComponentContainer())
    componentManager.register(SampleMemoryComponent())
    componentManager.register(SampleMessageComponent())
  }
}
