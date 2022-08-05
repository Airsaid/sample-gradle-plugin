package com.airsaid.sample.plugin.visitor.annotaiton

import com.airsaid.sample.plugin.visitor.SampleClassVisitor

class ExpandableAnnotationHandler(private val classList: List<String>) : AnnotationHandler() {
  override fun accept(classVisitor: SampleClassVisitor, desc: String, visible: Boolean): Boolean {
    if (classList.any { it == desc }) {
      whenFoundClass?.invoke(classVisitor)
      return true
    }
    return false
  }

  override fun visit(name: String, value: Any?) = Unit
}
