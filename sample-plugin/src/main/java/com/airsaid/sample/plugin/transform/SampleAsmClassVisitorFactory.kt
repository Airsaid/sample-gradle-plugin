package com.airsaid.sample.plugin.transform

import com.airsaid.sample.api.Extension
import com.airsaid.sample.api.PathDescription
import com.airsaid.sample.api.Register
import com.airsaid.sample.api.TestCase
import com.airsaid.sample.plugin.transform.processor.SampleProcessor
import com.airsaid.sample.plugin.util.isNeedProcessedClassName
import com.airsaid.sample.plugin.util.lifecycle
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.objectweb.asm.ClassVisitor

/**
 * @author airsaid
 */
abstract class SampleAsmClassVisitorFactory : AsmClassVisitorFactory<SampleAsmClassVisitorFactory.SampleParameters> {

  companion object {
    private val PROCESSED_ANNOTATION_CLASSES = listOf(
      Register::class.java,
      TestCase::class.java,
      Extension::class.java,
      PathDescription::class.java,
    )

    private val PROCESSED_ANNOTATION_CLASSES_NAME = PROCESSED_ANNOTATION_CLASSES.map { it.name }.toHashSet()
  }

  interface SampleParameters : InstrumentationParameters {
    /**
     * AGP will re-instrument dependencies, when the [InstrumentationParameters] changed
     * https://issuetracker.google.com/issues/190082518#comment4. This is just a dummy parameter
     * that is used solely for that purpose.
     */
    @get:Input
    @get:Optional
    val invalidate: Property<Long>
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
    return false
  }

  override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
    return SampleClassVisitor(nextClassVisitor, PROCESSED_ANNOTATION_CLASSES) { classData ->
      SampleProcessor.process(classData)
    }
  }
}
