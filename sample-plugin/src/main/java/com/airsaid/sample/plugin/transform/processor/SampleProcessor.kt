package com.airsaid.sample.plugin.transform.processor

import com.airsaid.sample.api.ExtensionItem
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.plugin.model.ClassData

/**
 * @author airsaid
 */
object SampleProcessor : ClassDataProcessor<Unit> {

  private val mSampleItems = hashMapOf<String, SampleItem>()
  private val mExtensionItems = hashMapOf<String, ExtensionItem>()

  private val mProcessors = listOf(
    SampleItemProcessor(),
    ExtensionItemProcessor()
  )

  override fun process(classData: ClassData) {
    mProcessors.forEach {
      val result = it.process(classData)
      if (result is SampleItem) {
        mSampleItems[classData.className] = result
      } else if (result is ExtensionItem) {
        mExtensionItems[classData.className] = result
      }
    }
  }

  fun getSampleItems(): List<SampleItem> = mSampleItems.values.toList()

  fun getExtensionItems(): List<ExtensionItem> = mExtensionItems.values.toList()
}
