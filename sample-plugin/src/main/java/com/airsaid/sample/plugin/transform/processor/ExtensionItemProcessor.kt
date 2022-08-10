package com.airsaid.sample.plugin.transform.processor

import com.airsaid.sample.api.Extension
import com.airsaid.sample.api.ExtensionItem
import com.airsaid.sample.plugin.model.ClassData

/**
 * @author airsaid
 */
class ExtensionItemProcessor : ClassDataProcessor<ExtensionItem?> {

  override fun process(classData: ClassData): ExtensionItem? {
    if (classData.classAnnotations.containsKey(Extension::class.java.name)) {
      return ExtensionItem(
        className = classData.className,
        superClassName = classData.superClassName,
        interfaces = classData.interfaces,
      )
    }
    return null
  }
}
