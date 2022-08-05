package com.airsaid.sample.extension.component.memory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author JackChen
 */
public class MemoryView extends CurveChartView {
  private static final long UPDATE_DURATION = 500;
  /**
   * cached array
   */
  private static final DalvikHeapMemoryInfo[] cached = new DalvikHeapMemoryInfo[3];

  /**
   * obtain a dalvik heap memory item.
   */
  public static DalvikHeapMemoryInfo obtain() {
    DalvikHeapMemoryInfo tl;
    synchronized (cached) {
      for (int i = cached.length; --i >= 0; ) {
        if (cached[i] != null) {
          tl = cached[i];
          cached[i] = null;
          return tl;
        }
      }
    }
    return new DalvikHeapMemoryInfo();
  }

  /**
   * recycler an memory information item.
   */
  public static void recycle(DalvikHeapMemoryInfo tl) {
    tl.recycler();
  }

  private final Runnable updateAction = new Runnable() {
    @Override
    public void run() {
      DalvikHeapMemoryInfo dalvikHeapMemory = getApplicationDalvikHeapMemory();
      addChartData(dalvikHeapMemory.allocated * 1f / 1024);
      recycle(dalvikHeapMemory);
      postDelayed(this, UPDATE_DURATION);
    }
  };


  public MemoryView(Context context) {
    super(context);
  }

  public MemoryView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MemoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    int visibility = getVisibility();
    if (VISIBLE == visibility) {
      startAnimation();
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    stopAnimation();
    super.onDetachedFromWindow();
  }

  @Override
  protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);
    if (VISIBLE == visibility) {
      startAnimation();
    } else {
      stopAnimation();
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);
    int visibility = getVisibility();
    if (hasWindowFocus && VISIBLE == visibility) {
      startAnimation();
    } else if (!hasWindowFocus) {
      stopAnimation();
    }
  }

  public void startAnimation() {
    removeCallbacks(updateAction);
    post(updateAction);
  }

  public void stopAnimation() {
    removeCallbacks(updateAction);
  }

  /**
   * Get application dalvik heap memory information
   */
  private DalvikHeapMemoryInfo getApplicationDalvikHeapMemory() {
    Runtime runtime = Runtime.getRuntime();
    DalvikHeapMemoryInfo dalvikHeapMem = obtain();
    dalvikHeapMem.freeMemory = runtime.freeMemory() / 1024;
    dalvikHeapMem.maxMemory = Runtime.getRuntime().maxMemory() / 1024;
    dalvikHeapMem.allocated = (Runtime.getRuntime().totalMemory() - runtime.freeMemory()) / 1024;
    return dalvikHeapMem;
  }

  /**
   * Dalvik heap memory information
   */
  static class DalvikHeapMemoryInfo {
    public long freeMemory;
    public long maxMemory;
    public long allocated;

    public void recycler() {
      freeMemory = 0;
      maxMemory = 0;
      allocated = 0;
    }
  }
}
