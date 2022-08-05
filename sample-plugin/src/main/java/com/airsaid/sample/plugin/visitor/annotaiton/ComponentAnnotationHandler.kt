package com.airsaid.sample.plugin.visitor.annotaiton

import com.airsaid.sample.api.Register
import com.airsaid.sample.api.SampleItem
import com.airsaid.sample.plugin.visitor.SampleClassVisitor

class ComponentAnnotationHandler : AnnotationHandler() {
  companion object {
    private val SAMPLE_ANNOTATION_DESC =
      "L" + Register::class.java.name.replace('.', '/') + ";"
  }
  var sampleItem: SampleItem? = null

  override fun accept(classVisitor: SampleClassVisitor, desc: String, visible: Boolean): Boolean {
    if (SAMPLE_ANNOTATION_DESC == desc) {
      sampleItem = SampleItem()
      whenFoundClass?.invoke(classVisitor)
      return true
    }
    return false
  }

  override fun visit(name: String, value: Any?) {
    when (name) {
      "title" -> sampleItem?.title = value.toString()
      "desc" -> sampleItem?.desc = value.toString()
      "path" -> sampleItem?.path = value.toString()
    }
  }
}
