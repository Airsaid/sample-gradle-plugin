package com.airsaid.sample.api

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * @author airsaid
 */
@Keep
@Serializable
data class SampleData(
  val sampleItems: List<SampleItem>,
  val extensionItems: List<ExtensionItem>
)
