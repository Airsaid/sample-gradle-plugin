package com.airsaid.sample.plugin.visitor.annotaiton

import com.airsaid.sample.plugin.visitor.SampleClassVisitor

abstract class AnnotationHandler {
  var whenFoundClass: ((SampleClassVisitor) -> Unit)? = null

  abstract fun accept(classVisitor: SampleClassVisitor, desc: String, visible: Boolean): Boolean
  abstract fun visit(name: String, value: Any?)

  fun whenFoundClass(closure: (SampleClassVisitor) -> Unit) {
    whenFoundClass = closure
  }
}
