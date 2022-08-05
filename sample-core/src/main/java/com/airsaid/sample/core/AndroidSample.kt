package com.airsaid.sample.core

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.ExtensionItem
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.component.ComponentManager
import com.airsaid.sample.core.extension.ExtensionHandler
import com.airsaid.sample.core.function.FunctionManager
import com.airsaid.sample.core.main.SampleActivityLifeCycleCallback
import com.airsaid.sample.core.model.PathNode
import com.airsaid.sample.core.processor.ActionProcessManager
import org.json.JSONArray
import org.json.JSONObject

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
      configurationField.get(null)?.let { configurationText ->
        val jsonObject = JSONObject(configurationText.toString())
        initialSampleItems(jsonObject)
        initialExtensions(jsonObject)
      }
    } catch (e: ClassNotFoundException) {
      e.printStackTrace()
      throw IllegalArgumentException(
        "Unable to find ${SampleConstants.SAMPLE_CONFIGURATION_CLASS} class," +
          " did you apply the `com.airsaid.sample` plugin?"
      )
    }
  }

  private fun initialSampleItems(jsonObject: JSONObject) {
    val sampleArray = jsonObject.getJSONArray("samples")
    val sampleList = deserializationSampleList(sampleArray)
    sampleItemList.addAll(sampleList)
    mergeSampleItems(sampleList)
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

  private fun deserializationSampleList(sampleArray: JSONArray): List<SampleItem> {
    val sampleItemList = mutableListOf<SampleItem>()
    for (i in 0 until sampleArray.length()) {
      val sampleItem = SampleItem()
      val sampleObject = sampleArray.getJSONObject(i)
      sampleItem.className = sampleObject.getString("className")
      val title = sampleObject.optString(SampleConstants.PARAMETER_TITLE)
      if (title.isNotEmpty()) {
        sampleItem.title = title
      } else {
        val simpleClassName = sampleItem.className.substringAfterLast(".")
        sampleItem.title = simpleClassName
      }
      sampleItem.desc = sampleObject.optString(SampleConstants.PARAMETER_DESC)
      sampleItem.path = sampleObject.optString(SampleConstants.PARAMETER_PATH)
      if (sampleItem.path.isEmpty()) {
        // Use the package as path.
        sampleItem.path = sampleItem.className.substringBeforeLast(".").replace('.', '/')
      }
      sampleItem.isTestCase = sampleObject.getBoolean("isTestCase")
      if (sampleItem.isAvailable) {
        sampleItemList.add(sampleItem)
      }
    }
    return sampleItemList
  }

  private fun initialExtensions(jsonObject: JSONObject) {
    val extensionArray = jsonObject.getJSONArray("extensions")
    val extensionList = deserializationExtensionList(extensionArray)
    extensionList.forEach { extensionItem ->
      extensionHandlers.values.forEach { extensionHandler ->
        extensionHandler.handle(
          extensionItem.className, extensionItem.superClass,
          extensionItem.interfaces.toList()
        )
      }
    }
  }

  private fun deserializationExtensionList(extensionsArray: JSONArray): List<ExtensionItem> {
    val extensionItemList = mutableListOf<ExtensionItem>()
    for (i in 0 until extensionsArray.length()) {
      val extensionObject = extensionsArray.getJSONObject(i)
      val extensionItem = ExtensionItem()
      extensionItem.className = extensionObject.optString("className")
      extensionItem.superClass = extensionObject.optString("superClass")
      val interfaceArray = extensionObject.getJSONArray("interfaces")
      extensionItem.interfaces = Array<String>(interfaceArray.length()) { index ->
        interfaceArray.getString(index)
      }
      if (extensionItem.isAvailable) {
        extensionItemList.add(extensionItem)
      }
    }
    return extensionItemList
  }

  fun getFunctionManager(): FunctionManager {
    return getExtension(FunctionManager::class.java)
  }

  fun getComponentManager(): ComponentManager {
    return getExtension(ComponentManager::class.java)
  }

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
