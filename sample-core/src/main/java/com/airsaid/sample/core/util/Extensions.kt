package com.airsaid.sample.core.util

import android.content.Context
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.model.PathNode
import com.airsaid.sample.core.path.PathProvider

fun Context.appName() =
  packageManager?.getPackageInfo(packageName, 0)?.applicationInfo?.loadLabel(packageManager)?.toString() ?: ""

fun PathNode.displayTitle(): String {
  val nodeItem = item
  if (nodeItem is SampleItem) {
    return nodeItem.title.replaceFirstChar { it.uppercaseChar() }
  }
  val itemStr = nodeItem.toString()
  val pathItems = PathProvider.pathItems
  val pathItem = pathItems[fullPath]
  return if (pathItem != null && pathItem.title.isNotEmpty()) {
    pathItem.title
  } else {
    itemStr.replaceFirstChar { it.uppercaseChar() }
  }
}

fun PathNode.displayDesc(): String {
  val nodeItem = item
  if (nodeItem is SampleItem) {
    return nodeItem.desc
  }
  val pathItems = PathProvider.pathItems
  val pathItem = pathItems[fullPath]
  return pathItem?.desc ?: ""
}
