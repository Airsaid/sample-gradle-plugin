package com.airsaid.sample.plugin.transform.processor

import com.airsaid.sample.api.Register
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.api.TestCase
import com.airsaid.sample.plugin.constant.Constants.PATH_SEPARATOR
import com.airsaid.sample.plugin.model.ClassData

/**
 * @author airsaid
 */
class SampleItemProcessor : ClassDataProcessor<SampleItem?> {

  override fun process(classData: ClassData): SampleItem? {
    if (classData.classAnnotations.containsKey(Register::class.java.name)) {
      val sampleItem = SampleItem(className = classData.className)
      classData.classAnnotations[Register::class.java.name]?.forEach { param ->
        when (param.name) {
          "title" -> sampleItem.title = param.value as String
          "desc" -> sampleItem.desc = param.value as String
          "path" -> sampleItem.path = param.value as String
        }
      }
      if (sampleItem.title.isEmpty()) {
        sampleItem.title = sampleItem.className.substringAfterLast('.')
      }
      if (sampleItem.path.isEmpty()) {
        sampleItem.path = sampleItem.className.substringBeforeLast('.').replace('.', PATH_SEPARATOR)
      }
      if (classData.classAnnotations.containsKey(TestCase::class.java.name)) {
        sampleItem.isTestCase = true
      }
      return sampleItem
    }
    return null
  }
}
