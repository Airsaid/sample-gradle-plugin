package com.airsaid.example.custom.processor

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Extension
import com.airsaid.sample.core.processor.SampleInterfaceProcessor

/**
 * Demonstrates how to customize the corresponding action to be
 * performed when the sample item is clicked.
 *
 * @author airsaid
 */
@Extension
class AlertDialogActionProcessor : SampleInterfaceProcessor<AlertDialog>() {
  override fun execute(context: AppCompatActivity, obj: AlertDialog) {
    obj.show()
  }
}
