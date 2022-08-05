package com.airsaid.sample.core.extension

/**
 * @author JackChen
 */
interface ExtensionHandler<E> {
  fun handle(className: String, superClass: String, interfaces: List<String>): Boolean

  fun register(extension: E)

  fun unregister(extension: E)
}
