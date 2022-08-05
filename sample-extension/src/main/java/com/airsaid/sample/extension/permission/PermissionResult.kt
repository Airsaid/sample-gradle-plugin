package com.airsaid.sample.extension.permission

class PermissionResult @JvmOverloads constructor(
  val name: String,
  val granted: Boolean,
  val shouldShowRequestPermissionRationale: Boolean = false
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as PermissionResult
    if (granted != that.granted) return false
    return if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) {
      false
    } else {
      name == that.name
    }
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + if (granted) 1 else 0
    result = 31 * result + if (shouldShowRequestPermissionRationale) 1 else 0
    return result
  }

  override fun toString(): String {
    return "Permission{" +
      "name='" + name + '\'' +
      ", granted=" + granted +
      ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
      '}'
  }

  private fun combineName(permissions: List<PermissionResult>): String {
    val output = StringBuilder()
    for (permission in permissions) {
      val name = permission.name
      if (name.isEmpty()) {
        output.append(name)
      } else {
        output.append(", ").append(name)
      }
    }
    return output.toString()
  }

  private fun combineGranted(permissions: List<PermissionResult>): Boolean {
    for (permission in permissions) {
      if (permission.granted) {
        return true
      }
    }
    return false
  }

  private fun combineShouldShowRequestPermissionRationale(permissions: List<PermissionResult>): Boolean {
    for (permission in permissions) {
      if (permission.shouldShowRequestPermissionRationale) {
        return true
      }
    }
    return false
  }
}
