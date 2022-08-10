package com.airsaid.sample.plugin.constant

import groovyjarjarasm.asm.Opcodes.ASM6

/**
 * @author airsaid
 */
object Constants {

  const val ASM_VERSION = ASM6
  const val PATH_SEPARATOR = '/'

  const val ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME = "androidx/appcompat/app/AppCompatActivity"
  const val ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME = "androidx/fragment/app/FragmentActivity"
  const val SAMPLE_COMPAT_ACTIVITY_CLASS_NAME = "com/airsaid/sample/core/appcompat/SampleAppCompatActivity"
}