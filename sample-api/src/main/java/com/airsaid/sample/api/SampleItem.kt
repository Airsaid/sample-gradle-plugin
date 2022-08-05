package com.airsaid.sample.api

import java.util.*

/**
 * @author airsaid
 */
data class SampleItem(
  var title: String = "",
  var desc: String = "",
  var path: String = "",
  var className: String = "",
  var isTestCase: Boolean = false,
) : Comparable<SampleItem> {

  val isAvailable: Boolean
    get() {
      try {
        Class.forName(className)
        return true
      } catch (e: ClassNotFoundException) {
        // Ignore
      }
      return false
    }

  fun clazz(): Class<*> = Class.forName(className)

  override fun compareTo(other: SampleItem): Int {
    return title.compareTo(other.title)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as SampleItem
    return className == that.className
  }

  override fun hashCode(): Int {
    return Objects.hash(className)
  }
}
