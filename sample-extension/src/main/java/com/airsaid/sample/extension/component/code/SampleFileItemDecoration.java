package com.airsaid.sample.extension.component.code;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airsaid.sample.extension.R;

/**
 * @author JackChen
 */
public class SampleFileItemDecoration extends RecyclerView.ItemDecoration {
  private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final int hierarchyOffset;

  public SampleFileItemDecoration(Context context) {
    Resources resources = context.getResources();
    hierarchyOffset = resources.getDimensionPixelOffset(R.dimen.sample_hierarchy_depth_offset);
    int strokeWidth = resources.getDimensionPixelOffset(R.dimen.sample_hierarchy_stroke_width);
    paint.setStrokeWidth(strokeWidth);
    paint.setColor(ContextCompat.getColor(context, R.color.sample_md_grey_400));
  }

  @Override
  public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
    super.onDrawOver(c, parent, state);
    int childCount = parent.getChildCount();
    int offsetLeft = parent.getPaddingLeft();
    int offsetTop = parent.getPaddingTop();
    for (int i = 0; i < childCount; i++) {
      View childView = parent.getChildAt(i);
      int left = hierarchyOffset + childView.getPaddingLeft();
      int top = childView.getTop() + childView.getHeight() / 2;
      c.drawLine(offsetLeft, offsetTop, offsetLeft, top, paint);
      c.drawLine(offsetLeft, top, left, top, paint);
    }
  }
}
