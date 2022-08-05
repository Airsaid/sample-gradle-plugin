package com.airsaid.sample.plugin.visitor

import com.airsaid.sample.plugin.instrumentation.SampleClassHandler
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * @author JackChen
 */
class SampleClassVisitor(classVisitor: ClassVisitor?) : ClassVisitor(Opcodes.ASM6, classVisitor) {
  companion object {
    private const val SUPER_CLASS_NAME = "com/airsaid/sample/core/appcompat/SampleAppCompatActivity"
    const val ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME = "androidx/appcompat/app/AppCompatActivity"
    const val ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME = "androidx/fragment/app/FragmentActivity"
  }

  private lateinit var superClass: String
  private lateinit var className: String
  private var interfaceArray = emptyArray<String>()

  override fun visit(
    version: Int,
    access: Int,
    name: String,
    signature: String?,
    superName: String,
    interfaces: Array<String>
  ) {
    className = name
    superClass = superName
    interfaceArray = interfaces
    // Here we change the super class name to our appcompat class.
    if (ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME == superName || ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME == superName) {
      super.visit(version, access, name, signature, SUPER_CLASS_NAME, interfaces)
    } else {
      super.visit(version, access, name, signature, superName, interfaces)
    }
  }

  override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
    val annotationHandler = SampleClassHandler.accept(this, desc, visible)
    if (null != annotationHandler) {
      return object : AnnotationVisitor(Opcodes.ASM6, super.visitAnnotation(desc, visible)) {
        override fun visit(name: String, value: Any?) {
          super.visit(name, value)
          annotationHandler.visit(name, value)
        }
      }
    }
    return super.visitAnnotation(desc, visible)
  }

  fun getClassName() = className.replace('/', '.')

  fun getSuperClassName() = superClass.replace('/', '.')

  fun getInterfaces() = interfaceArray
}
