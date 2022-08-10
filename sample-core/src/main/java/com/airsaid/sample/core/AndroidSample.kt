package com.airsaid.sample.core

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.SampleData
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.component.ComponentManager
import com.airsaid.sample.core.extension.ExtensionHandler
import com.airsaid.sample.core.function.FunctionManager
import com.airsaid.sample.core.main.SampleActivityLifeCycleCallback
import com.airsaid.sample.core.model.PathNode
import com.airsaid.sample.core.processor.ActionProcessManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * @author JackChen
 */
object AndroidSample {

  private lateinit var applicationContext: Context

  private val rootNode = PathNode(".")
  private val actionProcessorManager = ActionProcessManager
  private val sampleItemList = mutableListOf<SampleItem>()
  private val extensionHandlers = mutableMapOf<Class<*>, ExtensionHandler<*>>()

  init {
    extensionHandlers[FunctionManager::class.java] = FunctionManager
    extensionHandlers[ComponentManager::class.java] = ComponentManager
    extensionHandlers[ActionProcessManager::class.java] = actionProcessorManager
  }

  fun attachToContext(context: Context) {
    val application = context.applicationContext as Application
    application.registerActivityLifecycleCallbacks(SampleActivityLifeCycleCallback())
    applicationContext = application

    try {
      val configurationClass = Class.forName(SampleConstants.SAMPLE_CONFIGURATION_CLASS)
      val configurationField = configurationClass.getField(SampleConstants.SAMPLE_CONFIGURATION_FIELD_NAME)
      configurationField.get(null)?.let { configurationJson ->
        val sampleData = Json.decodeFromString<SampleData>(configurationJson as String)

        sampleItemList.addAll(sampleData.sampleItems)
        mergeSampleItems(sampleItemList)

        sampleData.extensionItems.forEach { extensionItem ->
          extensionHandlers.values.forEach { extensionHandler ->
            extensionHandler.handle(
              extensionItem.className, extensionItem.superClassName,
              extensionItem.interfaces.toList()
            )
          }
        }
      }
    } catch (e: ClassNotFoundException) {
      e.printStackTrace()
      throw IllegalArgumentException(
        "Unable to find ${SampleConstants.SAMPLE_CONFIGURATION_CLASS} class," +
          " did you apply the 'com.airsaid.sample' plugin?"
      )
    }
  }

  private fun mergeSampleItems(sampleItemList: List<SampleItem>) {
    val root = rootNode
    val map = mutableMapOf<String, PathNode>()
    sampleItemList.forEach { sampleItem ->
      var prevNode = root
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
    // Collect the single node in the path.
    val singleNodeList = mutableListOf<PathNode>()
    collectSingleNode(singleNodeList, root, root.isSingle())
    // Make the single node as simple as possible.
    singleNodeList.forEach(PathNode::shiftUp)
    rootNode.children.forEach { childNode ->
      shrinkPathNode(rootNode, childNode)
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

  fun getFunctionManager(): FunctionManager {
    return getExtension(FunctionManager::class.java)
  }

  fun getComponentManager(): ComponentManager {
    return getExtension(ComponentManager::class.java)
  }

  @Suppress("UNCHECKED_CAST")
  fun <E> getExtension(clazz: Class<E>): E {
    return extensionHandlers[clazz] as E
  }

  fun getTestCases(): List<SampleItem> {
    val testcases: MutableList<SampleItem> = ArrayList()
    for (sampleItem in sampleItemList) {
      if (sampleItem.isTestCase) {
        testcases.add(sampleItem)
      }
    }
    return testcases
  }

  fun registerExtensionHandler(extensionHandler: ExtensionHandler<*>) {
    extensionHandlers[extensionHandler::class.java] = extensionHandler
  }

  fun getPathNodeList(path: String?): List<PathNode> {
    val pathNodeList = if (null == path)
      rootNode.children else rootNode.path(path)
    return pathNodeList.sortedBy {
      val item = it.item
      if (item is SampleItem) item.title else item.toString()
    }
  }

  fun start(context: AppCompatActivity, item: SampleItem) {
    val functionManager = getFunctionManager()
    actionProcessorManager.process(functionManager, context, item)
  }
}
