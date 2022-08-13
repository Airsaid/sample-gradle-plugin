package com.airsaid.sample.api

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import java.util.Objects

/**
 * @author airsaid
 */
@Keep
@Serializable
data class SampleItem(
  val className: String,
  var title: String = "",
  var desc: String = "",
  var path: String = "",
  var isTestCase: Boolean = false,
) : Comparable<SampleItem> {

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
