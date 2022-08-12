package com.airsaid.sample.plugin.transform.processor

import com.airsaid.sample.plugin.model.ClassData

/**
 * @author airsaid
 */
interface ClassDataProcessor<R> {

  fun process(classData: ClassData): R
}
