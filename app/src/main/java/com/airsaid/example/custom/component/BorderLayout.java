package com.airsaid.example.custom.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * @author JackChen
 */
public class BorderLayout extends FrameLayout {
  private final Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Rect outRect = new Rect();
  private final Path path = new Path();

  public BorderLayout(Context context) {
    this(context, null, 0);
  }

  public BorderLayout(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BorderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setWillNotDraw(false);

    path.moveTo(0f, 20f);
    path.lineTo(0f, 0f);
    path.lineTo(20f, 0f);

    paint1.setStyle(Paint.Style.STROKE);
    paint1.setStrokeWidth(1f);
    paint1.setColor(Color.RED);

    paint2.setStyle(Paint.Style.STROKE);
    paint2.setStrokeWidth(3f);
    paint2.setColor(Color.BLUE);
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    drawChildViewBorder(canvas, this);
  }

  /**
   * Draw children view border.
   */
  private void drawChildViewBorder(Canvas canvas, View childView) {
    childView.getHitRect(outRect);
    canvas.drawRect(outRect, paint1);
    drawBorderPath(canvas, 0f, outRect.left, outRect.top); // left top
    drawBorderPath(canvas, 90f, outRect.right, outRect.top); // right top
    drawBorderPath(canvas, 270f, outRect.left, outRect.bottom); // left bottom
    drawBorderPath(canvas, 180f, outRect.right, outRect.bottom); // right bottom
    // continues loop
    if (childView instanceof ViewGroup) {
      ViewGroup childViewGroup = (ViewGroup) childView;
      for (int i = 0; i < childViewGroup.getChildCount(); i++) {
        View v = childViewGroup.getChildAt(i);
        drawChildViewBorder(canvas, v);
      }
    }
  }

  /**
   * draw border path.
   */
  private void drawBorderPath(Canvas canvas, float rotate, float left, float top) {
    canvas.save();
    canvas.translate(left, top);
    canvas.rotate(rotate);
    canvas.drawPath(path, paint2);
    canvas.restore();
  }
}
