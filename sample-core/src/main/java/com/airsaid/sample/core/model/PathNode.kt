package com.airsaid.sample.core.model

/**
 * @author JackChen
 */
class PathNode(var item: Any? = null, var fullPath: String? = null) {
  var parent: PathNode? = null
  var children: ArrayList<PathNode> = ArrayList(1)

  fun contains(pathNode: PathNode): Boolean {
    return children.contains(pathNode)
  }

  fun add(pathNode: PathNode) {
    children.add(pathNode)
    pathNode.parent = this
  }

  fun remove(node: PathNode?) {
    children.remove(node)
  }

  fun isEmpty(): Boolean {
    return children.isEmpty()
  }

  fun path(pathStr: String): List<PathNode> {
    val pathList = pathStr.split("/")
    var children = children
    pathList.forEach { path ->
      val child = children.find { it.item == path }
      if (null != child) {
        children = child.children
      }
    }
    return children
  }

  fun fullPath(): String {
    fullPath?.also { return it }
    if (item == ".") return ""
    val parentFullPath = parent?.fullPath() ?: ""
    if (parentFullPath.isNotEmpty()) {
      return "$parentFullPath/$item"
    }
    return item.toString()
  }

  fun shiftUp() {
    if (null == parent) return
    parent?.remove(this)
    val grandParent = parentOf(parent)
    grandParent?.children?.add(this)
    if (true == parent?.isEmpty()) {
      grandParent?.remove(parent)
    }
    parent = grandParent
    if (null != parentOf(parent) && true == parent?.isSingle()) {
      shiftUp()
    }
  }

  private fun parentOf(p: PathNode?): PathNode? {
    return p?.parent
  }

  fun isSingle(): Boolean {
    return 1 == children.size
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as PathNode
    if (item != other.item) return false
    return true
  }

  override fun hashCode(): Int {
    return item?.hashCode() ?: 0
  }

  override fun toString(): String {
    return item?.toString() ?: super.toString()
  }
}
