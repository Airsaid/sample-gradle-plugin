package com.airsaid.sample.extension.component.memory;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.airsaid.sample.extension.R;

import java.util.LinkedList;

/**
 * @author JackChen
 */
class CurveChartView extends View {
  private static final int MAX_DATA_SIZE = 30;
  private static final float MAX_VALUE_MULTI = 1.2f;
  private static final float MIN_VALUE_MULTI = 0.8f;

  private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint graduatedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
  private final Path linePath = new Path();
  private final Path fillPath = new Path();
  private float yAxisMaxValue = 0f;
  private float yAxisMinValue = 0f;

  private float partCount = 0;
  private final LinkedList<Float> dataArray = new LinkedList<>();

  public CurveChartView(Context context) {
    this(context, null, R.attr.sampleCurveChartView);
  }

  public CurveChartView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, R.attr.sampleCurveChartView);
  }

  public CurveChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleCurveChartView, defStyleAttr, R.style.Sample_CurveChartViewCompat);
    setColor(a.getColor(R.styleable.SampleCurveChartView_sample_curveColor, 0));
    setFillColor(a.getColor(R.styleable.SampleCurveChartView_sample_curveFillColor, 0));
    setLabelTextSize(a.getDimensionPixelSize(R.styleable.SampleCurveChartView_sample_curveLabelTextSize, 0));
    setStrokeWidth(a.getDimension(R.styleable.SampleCurveChartView_sample_curveStrokeWidth, 0f));
    setPartCount(a.getInteger(R.styleable.SampleCurveChartView_sample_curveChartCount, 0));
    a.recycle();
  }

  private void setColor(int color) {
    int newColor = Color.argb(0x44, Color.red(color), Color.green(color), Color.blue(color));
    linePaint.setColor(newColor);
    graduatedLinePaint.setColor(newColor);
    textPaint.setColor(newColor);
  }

  private void setFillColor(int color) {
    fillPaint.setColor(Color.argb(0x22, Color.red(color), Color.green(color), Color.blue(color)));
  }

  private void setLabelTextSize(float textSize) {
    textPaint.setTextSize(textSize);
  }

  private void setStrokeWidth(float strokeWidth) {
    this.linePaint.setStyle(Paint.Style.STROKE);
    this.linePaint.setStrokeWidth(strokeWidth);
    this.graduatedLinePaint.setStrokeWidth(strokeWidth);
  }

  private void setPartCount(int count) {
    this.partCount = count;
  }

  public void addChartData(float data) {
    this.dataArray.offer(data);
    if (MAX_DATA_SIZE < dataArray.size()) {
      dataArray.pollFirst();
    }
    float maxValue = Float.MIN_VALUE;
    float minValue = Float.MAX_VALUE;
    for (Float v : dataArray) {
      if (v > maxValue) {
        maxValue = v;
      }
      if (v < minValue) {
        minValue = v;
      }
    }
    yAxisMaxValue = MAX_VALUE_MULTI * maxValue;
    yAxisMinValue = MIN_VALUE_MULTI * minValue;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawXY(canvas);
    drawScaleLabel(canvas);
    if (!dataArray.isEmpty()) {
      drawLine(canvas, dataArray.size());
    }
  }

  private void drawXY(Canvas canvas) {
    int width = getWidth();
    int height = getHeight();
    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int paddingRight = getPaddingRight();
    int paddingBottom = getPaddingBottom();
    canvas.drawLine(paddingLeft, paddingTop, paddingLeft, height - paddingBottom, linePaint);
    canvas.drawLine(paddingLeft,
        height - paddingBottom * 1f,
        width - paddingRight * 1f,
        height - paddingBottom * 1f, linePaint);
  }

  private void drawScaleLabel(Canvas canvas) {
    int width = getWidth();
    int height = getHeight();
    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int paddingRight = getPaddingRight();
    int paddingBottom = getPaddingBottom();
    float yIntervalLen = (height - paddingBottom - paddingTop) / (partCount - 1);
    for (int i = 0; i < partCount; i++) {
      float scaleY = height - paddingBottom * 1f - yIntervalLen * i;
      canvas.drawLine(paddingLeft, scaleY, (width - paddingRight), scaleY, graduatedLinePaint);
      String label = String.format("%.1fM", (this.yAxisMaxValue - this.yAxisMinValue) * i * 1f / (partCount - 1) + this.yAxisMinValue);
      canvas.drawText(label, paddingLeft, scaleY, textPaint);
    }
  }

  private void drawLine(Canvas canvas, int size) {
    int height = getHeight();
    int paddingLeft = getPaddingLeft();
    int paddingBottom = getPaddingBottom();
    float axisXLengthPerValue = getAxisXLengthPerValue();
    float axisYLengthPerValue = getAxisYLengthPerValue();

    this.linePath.reset();
    this.fillPath.reset();
    this.linePath.moveTo(paddingLeft, height - paddingBottom * 1f - (dataArray.get(0) - this.yAxisMinValue) * axisYLengthPerValue);
    this.fillPath.moveTo(paddingLeft, height - paddingBottom * 1f);
    this.fillPath.lineTo(paddingLeft, height - paddingBottom * 1f - (dataArray.get(0) - this.yAxisMinValue) * axisYLengthPerValue);

    for (int i = 1; i < size; i++) {
      float value = dataArray.get(i);
      this.linePath.lineTo(paddingLeft + i * axisXLengthPerValue, height - paddingBottom * 1f - (value - this.yAxisMinValue) * axisYLengthPerValue);
      this.fillPath.lineTo(paddingLeft + i * axisXLengthPerValue, height - paddingBottom * 1f - (value - this.yAxisMinValue) * axisYLengthPerValue);
    }
    this.fillPath.lineTo(paddingLeft + (size - 1) * axisXLengthPerValue, height - paddingBottom * 1f);
    this.fillPath.close();
    canvas.drawPath(this.linePath, linePaint);
    canvas.drawPath(this.fillPath, fillPaint);
  }


  private float getAxisXLengthPerValue() {
    int width = getWidth();
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();
    return (width - paddingLeft - paddingRight) / (MAX_DATA_SIZE - 1);
  }

  private float getAxisYLengthPerValue() {
    int height = getHeight();
    int paddingTop = getPaddingTop();
    int paddingBottom = getPaddingBottom();
    return (height - paddingBottom - paddingTop) / (yAxisMaxValue - yAxisMinValue);
  }
}
