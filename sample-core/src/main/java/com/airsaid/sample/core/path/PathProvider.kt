package com.airsaid.sample.core.path

import com.airsaid.sample.api.PathItem

/**
 * @author airsaid
 */
object PathProvider {

  val pathItems = hashMapOf<String, PathItem>()

  fun init(paths: List<PathItem>) {
    paths.forEach { item ->
      pathItems[item.path] = item
    }
  }
}
