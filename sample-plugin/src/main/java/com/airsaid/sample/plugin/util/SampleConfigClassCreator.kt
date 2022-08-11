package com.airsaid.sample.plugin.util

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileOutputStream

/**
 * @author airsaid
 */
object SampleConfigClassCreator {
  private const val CONFIGURATION_CLASS_NAME = "SampleConfiguration"
  private const val CONFIGURATION_CLASS_PATH = "com/airsaid/sample/core/$CONFIGURATION_CLASS_NAME"

  @Throws(Exception::class)
  fun create(outputFile: File, configurationJsonText: String) {
    val classWriter = ClassWriter(0)
    classWriter.visit(
      Opcodes.V11,
      Opcodes.ACC_PUBLIC or Opcodes.ACC_SUPER,
      CONFIGURATION_CLASS_PATH,
      null,
      "java/lang/Object",
      null
    )
    classWriter.visitSource("$CONFIGURATION_CLASS_NAME.java", null)

    val fieldVisitor = classWriter.visitField(
      Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
      "CONFIGURATION_JSON",
      "Ljava/lang/String;",
      null,
      configurationJsonText
    )
    fieldVisitor.visitEnd()

    val methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
    methodVisitor.visitCode()
    val label0 = Label()
    methodVisitor.visitLabel(label0)
    methodVisitor.visitLineNumber(3, label0)
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
    methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
    methodVisitor.visitInsn(Opcodes.RETURN)
    val label1 = Label()
    methodVisitor.visitLabel(label1)
    methodVisitor.visitLocalVariable("this", "L$CONFIGURATION_CLASS_PATH;", null, label0, label1, 0)
    methodVisitor.visitMaxs(1, 1)
    methodVisitor.visitEnd()

    classWriter.visitEnd()

    val classFile = File(outputFile, "$CONFIGURATION_CLASS_PATH.class")
    if (!classFile.parentFile.exists()) {
      classFile.parentFile.mkdirs()
    }
    FileOutputStream(classFile).use { fos -> fos.write(classWriter.toByteArray()) }
  }
}
