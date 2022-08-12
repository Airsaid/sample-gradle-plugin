package com.airsaid.sample.plugin.transform

import com.airsaid.sample.plugin.constant.Constants.ASM_VERSION
import com.airsaid.sample.plugin.constant.Constants.SAMPLE_COMPAT_ACTIVITY_CLASS_NAME
import com.airsaid.sample.plugin.model.ClassData
import com.airsaid.sample.plugin.util.isNeedProcessedClassName
import com.airsaid.sample.plugin.util.toFullyQualifiedName
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Type

/**
 * @author airsaid
 */
class SampleClassVisitor(
  cv: ClassVisitor? = null,
  private val processedAnnotations: List<Class<*>>,
  private val resultCallback: (ClassData) -> Unit,
) : ClassVisitor(ASM_VERSION, cv) {

  private lateinit var mClassData: ClassData

  private val mAnnotationMap = hashMapOf<String, List<ClassData.AnnotationParam>>()

  override fun visit(
    version: Int,
    access: Int,
    name: String,
    signature: String?,
    superName: String,
    interfaces: Array<out String>?
  ) {
    val interfaceList = (interfaces?.toList() ?: emptyList()).map { it.toFullyQualifiedName() }
    mClassData = ClassData(
      name.toFullyQualifiedName(), superName.toFullyQualifiedName(),
      mAnnotationMap, interfaceList
    )
    // Here we change the super class name to our appcompat class
    if (superName.isNeedProcessedClassName()) {
      super.visit(version, access, name, signature, SAMPLE_COMPAT_ACTIVITY_CLASS_NAME, interfaces)
    } else {
      super.visit(version, access, name, signature, superName, interfaces)
    }
  }

  override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
    val av = super.visitAnnotation(descriptor, visible)?.also {
      processedAnnotations.forEach { annotationClass ->
        if (descriptor == Type.getDescriptor(annotationClass)) {
          val annotationClassName = Type.getType(descriptor).className
          val annotationParams = mutableListOf<ClassData.AnnotationParam>()
          mAnnotationMap[annotationClassName] = annotationParams
          return SampleAnnotationAdapter(annotationParams, it)
        }
      }
    }
    return av
  }

  override fun visitEnd() {
    if (mAnnotationMap.isNotEmpty()) {
      resultCallback(mClassData)
    }
    super.visitEnd()
  }

  private class SampleAnnotationAdapter(
    private val params: MutableList<ClassData.AnnotationParam>,
    av: AnnotationVisitor? = null
  ) : AnnotationVisitor(ASM_VERSION, av) {
    override fun visit(name: String, value: Any?) {
      params.add(ClassData.AnnotationParam(name, value))
      super.visit(name, value)
    }
  }
}
