package com.airsaid.sample.core.component

import com.airsaid.sample.core.extension.ExtensionHandler
import java.util.*

/**
 * @author JackChen
 */
object ComponentManager : ExtensionHandler<ComponentContainer> {
  private val COMPONENT_CONTAINER_CLASS_DESC = ComponentContainer::class.java.name.replace('.', '/')

  /**
   * The extra component list that will decorate every sample
   */
  private val componentContainerSet: MutableSet<ComponentContainer> =
    TreeSet { c1, c2 ->
      val i = c1.getComponentPriority() - c2.getComponentPriority()
      if (0 == i) -1 else i
    }

  override fun handle(
    className: String,
    superClass: String,
    interfaces: List<String>
  ): Boolean {
    if (interfaces.contains(COMPONENT_CONTAINER_CLASS_DESC)) {
      try {
        val clazz = Class.forName(className)
        val componentContainer = clazz.newInstance() as ComponentContainer
        register(componentContainer)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      return true
    }
    return false
  }

  override fun register(extension: ComponentContainer) {
    componentContainerSet.add(extension)
  }

  override fun unregister(extension: ComponentContainer) {
    componentContainerSet.remove(extension)
  }

  fun getComponentContainerSet(): Set<ComponentContainer> {
    return componentContainerSet
  }
}
