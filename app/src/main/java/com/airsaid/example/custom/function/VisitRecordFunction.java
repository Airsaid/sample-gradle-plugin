package com.airsaid.example.custom.function;

import android.util.Log;
import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import com.airsaid.sample.api.Extension;
import com.airsaid.sample.api.SampleItem;
import com.airsaid.sample.core.function.SampleFunction;

/**
 * @author JackChen
 */
@Keep
@Extension
public class VisitRecordFunction implements SampleFunction {
    private static final String TAG = "VisitRecordFunction";

    @Override public void onInitialize(final AppCompatActivity context) {
    }

    @Override
    public boolean isAvailable(Class<?> clazz) {
        return true;
    }

    @Override
    public void execute(AppCompatActivity context, SampleItem item) {
        Log.e(TAG, "Title:" + item.getTitle() + " class:" + item.getClassName());
    }
}
