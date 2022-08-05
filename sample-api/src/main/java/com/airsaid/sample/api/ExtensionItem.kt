package com.airsaid.sample.api

import java.util.*

/**
 * @author airsaid
 */
data class ExtensionItem(
  var className: String = "",
  var superClass: String = "",
  var interfaces: Array<String> = emptyArray(),
) {

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

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as ExtensionItem
    return className == that.className
  }

  override fun hashCode(): Int {
    return Objects.hash(className)
  }
}
