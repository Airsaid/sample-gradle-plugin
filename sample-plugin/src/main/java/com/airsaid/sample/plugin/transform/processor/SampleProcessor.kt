package com.airsaid.sample.plugin.transform.processor

import com.airsaid.sample.api.ExtensionItem
import com.airsaid.sample.api.PathItem
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.plugin.model.ClassData

/**
 * @author airsaid
 */
object SampleProcessor : ClassDataProcessor<Unit> {

  private val mSampleItems = hashMapOf<String, SampleItem>()
  private val mExtensionItems = hashMapOf<String, ExtensionItem>()
  private val mPathItems = hashMapOf<String, PathItem>()

  private val mProcessors = listOf(
    SampleItemProcessor(),
    ExtensionItemProcessor(),
    PathItemProcessor(),
  )

  override fun process(classData: ClassData) {
    mProcessors.forEach {
      val result = it.process(classData)
      if (result is SampleItem) {
        mSampleItems[classData.className] = result
      } else if (result is ExtensionItem) {
        mExtensionItems[classData.className] = result
      } else if (result is PathItem) {
        mPathItems[classData.className] = result
      }
    }
  }

  fun getSampleItems(): List<SampleItem> = mSampleItems.values.toList()

  fun getExtensionItems(): List<ExtensionItem> = mExtensionItems.values.toList()

  fun getPathItems(): List<PathItem> = mPathItems.values.toList()

  /**
   * For incremental scenarios, if the new class no longer matches,
   * it needs to be removed from the list to avoid generating dirty data.
   *
   * @param notMatchClassName Class names that do not conform to the processing rules.
   */
  fun cleanDirtyData(notMatchClassName: String) {
    if (mSampleItems.containsKey(notMatchClassName)) {
      mSampleItems.remove(notMatchClassName)
    }
    if (mExtensionItems.containsKey(notMatchClassName)) {
      mExtensionItems.remove(notMatchClassName)
    }
    if (mPathItems.containsKey(notMatchClassName)) {
      mPathItems.remove(notMatchClassName)
    }
  }
}
