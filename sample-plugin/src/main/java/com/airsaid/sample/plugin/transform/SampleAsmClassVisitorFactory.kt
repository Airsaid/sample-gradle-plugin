package com.airsaid.sample.plugin.transform

import com.airsaid.sample.api.Extension
import com.airsaid.sample.api.PathDescription
import com.airsaid.sample.api.Register
import com.airsaid.sample.api.TestCase
import com.airsaid.sample.plugin.transform.processor.SampleProcessor
import com.airsaid.sample.plugin.util.isNeedProcessedClassName
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

/**
 * @author airsaid
 */
abstract class SampleAsmClassVisitorFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {

  companion object {
    private val PROCESSED_ANNOTATION_CLASSES = listOf(
      Register::class.java,
      TestCase::class.java,
      Extension::class.java,
      PathDescription::class.java,
    )

    private val PROCESSED_ANNOTATION_CLASSES_NAME = PROCESSED_ANNOTATION_CLASSES.map { it.name }.toHashSet()
  }

  override fun isInstrumentable(classData: ClassData): Boolean {
    val superClassName = classData.superClasses[0]
    if (superClassName.isNeedProcessedClassName()) {
      return true
    }
    classData.classAnnotations.forEach {
      if (PROCESSED_ANNOTATION_CLASSES_NAME.contains(it)) {
        return true
      }
    }
    SampleProcessor.cleanDirtyData(classData.className)
    return false
  }

  override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
    return SampleClassVisitor(nextClassVisitor, PROCESSED_ANNOTATION_CLASSES) { classData ->
      SampleProcessor.process(classData)
    }
  }
}
