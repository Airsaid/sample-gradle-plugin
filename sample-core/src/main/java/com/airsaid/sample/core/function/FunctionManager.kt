package com.airsaid.sample.core.function

import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.core.extension.ExtensionHandler

/**
 * @author JackChen
 * @see SampleFunction
 */
object FunctionManager : ExtensionHandler<SampleFunction> {
  private val FUNCTION_CLASS_DESC = SampleFunction::class.java.name.replace('.', '/')

  /**
   * All the action plugin
   */
  private val functionList: MutableList<SampleFunction> = ArrayList()

  override fun handle(
    className: String,
    superClass: String,
    interfaces: List<String>
  ): Boolean {
    if (interfaces.contains(FUNCTION_CLASS_DESC)) {
      try {
        val clazz = Class.forName(className)
        val function = clazz.newInstance() as SampleFunction
        register(function)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      return true
    }
    return false
  }

  /**
   * Register a plugin. It will put new plugin to list.
   */
  override fun register(extension: SampleFunction) {
    functionList.add(extension)
  }

  /**
   * Unregister a plugin, It will remove the plugin from list.
   */
  override fun unregister(extension: SampleFunction) {
    functionList.remove(extension)
  }

  fun getFunctionList(): List<SampleFunction> {
    return functionList
  }

  fun execute(context: AppCompatActivity, item: SampleItem) {
    for (function in functionList) {
      val clazz = item.clazz()
      if (function.isAvailable(clazz)) {
        function.execute(context, item)
      }
    }
  }
}
