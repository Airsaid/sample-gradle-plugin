package com.airsaid.sample.api

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import java.util.Objects

/**
 * @author airsaid
 */
@Keep
@Serializable
data class PathItem(
  val path: String,
  val title: String,
  val desc: String,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as PathItem
    return path == that.path
  }

  override fun hashCode(): Int {
    return Objects.hash(path)
  }
}
