package com.airsaid.sample.plugin.util

import com.airsaid.sample.api.SampleItem

object PathNodePrinter {
  fun printPathTree(sampleList: List<SampleItem>) {
    val rootNode = PathNode(".")
    mergeSampleItems(rootNode, sampleList)
    // Collect the single node in the path.
    val singleNodeList = mutableListOf<PathNode>()
    collectSingleNode(singleNodeList, rootNode, rootNode.isSingle())
    // Make the single node as simple as possible.
    singleNodeList.forEach(PathNode::shiftUp)
    rootNode.children.toList().forEach { childNode ->
      shrinkPathNode(rootNode, childNode)
    }
    println()
    val pathLength = sampleList.maxOf { sampleItem -> sampleItem.path.length }
    sampleList.forEach { sampleItem ->
      println("+--- " + sampleItem.path.padEnd(pathLength) + " " + sampleItem.title)
    }
    println()
    traversalPathTree(rootNode, 0)
  }

  private fun mergeSampleItems(rootNode: PathNode, sampleItemList: List<SampleItem>) {
    val map = mutableMapOf<String, PathNode>()
    sampleItemList.forEach { sampleItem ->
      var prevNode = rootNode
      if (sampleItem.path.isEmpty()) {
        // Use the package as path.
        sampleItem.path = sampleItem.className.substringBeforeLast(".").replace('.', '/')
      }
      val pathList = sampleItem.path.split("/")
      pathList.forEach { path ->
        var pathNode = map[path]
        if (null == pathNode) {
          pathNode = PathNode(path)
          map[path] = pathNode
          prevNode.add(pathNode)
        }
        if (false == pathNode.parent?.contains(pathNode)) {
          pathNode.parent?.add(pathNode)
          prevNode.add(pathNode)
        }
        prevNode = pathNode
      }
      prevNode.add(PathNode(sampleItem))
    }
  }

  private fun collectSingleNode(
    singleNodeList: MutableList<PathNode>,
    node: PathNode,
    singleNode: Boolean
  ) {
    node.children.forEach { child ->
      collectSingleNode(singleNodeList, child, node.isSingle())
    }
    if (node.children.isEmpty() && singleNode) {
      singleNodeList.add(node)
    }
  }

  private fun shrinkPathNode(parentNode: PathNode, currentNode: PathNode) {
    currentNode.children.toList().forEach { childNode ->
      shrinkPathNode(currentNode, childNode)
    }
    if (0 == currentNode.children.size && currentNode.item is String) {
      parentNode.remove(currentNode)
    }
    if (1 == parentNode.children.size && currentNode.item is String) {
      parentNode.remove(currentNode)
      currentNode.children.toList().forEach { childNode ->
        parentNode.add(childNode)
      }
    }
  }

  private fun traversalPathTree(pathNode: PathNode, depth: Int) {
    val nodeItem = pathNode.item
    if (nodeItem is SampleItem) {
      println("|" + "\t".repeat(depth) + "\t+---" + nodeItem.title)
    } else {
      println("|" + "\t".repeat(depth) + "\t\\---" + nodeItem.toString())
    }
    val childNodeList = pathNode.children.sortedBy {
      val item = it.item
      if (item is SampleItem) item.title else item.toString()
    }
    childNodeList.forEach { childNode ->
      traversalPathTree(childNode, depth + 1)
    }
  }

  class PathNode(var item: Any? = null) {
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
}
