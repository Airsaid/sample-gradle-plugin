package com.airsaid.sample.plugin.transform.processor

import com.airsaid.sample.api.PathDescription
import com.airsaid.sample.api.PathItem
import com.airsaid.sample.plugin.model.ClassData
import com.airsaid.sample.plugin.util.toFullyQualifiedPath

/**
 * @author airsaid
 */
class PathItemProcessor : ClassDataProcessor<PathItem?> {
  override fun process(classData: ClassData): PathItem? {
    if (classData.classAnnotations.containsKey(PathDescription::class.java.name)) {
      var path = classData.className.substringBeforeLast('.').toFullyQualifiedPath()
      var title = ""
      var desc = ""
      classData.classAnnotations[PathDescription::class.java.name]?.forEach { param ->
        when (param.name) {
          "path" -> path = param.value as String
          "title" -> title = param.value as String
          "desc" -> desc = param.value as String
        }
      }
      return PathItem(path, title, desc)
    }
    return null
  }
}