package com.airsaid.sample.core.exception

import com.airsaid.sample.api.SampleItem

/**
 * @author airsaid
 */
class SampleFailedException(
  val sampleItem: SampleItem,
  cause: Throwable? = null
) : RuntimeException(sampleItem.toString(), cause)
