package com.airsaid.example.custom.processor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airsaid.sample.core.exception.SampleFailedException;
import com.airsaid.sample.core.processor.SampleInterfaceProcessor;
import com.airsaid.sample.api.Extension;

/**
 * @author JackChen
 */
@Extension
public class AlertDialogActionProcessor extends SampleInterfaceProcessor<AlertDialog> {
  @Override
  public void execute(@NonNull final AppCompatActivity context, final AlertDialog dialog)
      throws SampleFailedException {
    dialog.show();
  }
}
