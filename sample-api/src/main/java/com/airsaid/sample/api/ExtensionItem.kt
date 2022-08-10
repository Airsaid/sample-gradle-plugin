package com.airsaid.sample.api

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import java.util.Objects

/**
 * @author airsaid
 */
@Keep
@Serializable
data class ExtensionItem(
  val className: String,
  val superClassName: String,
  val interfaces: List<String>,
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
