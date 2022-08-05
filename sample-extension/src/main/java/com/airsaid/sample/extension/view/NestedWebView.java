package com.airsaid.sample.extension.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

/**
 * @author JackChen
 */
public class NestedWebView extends WebView implements NestedScrollingChild {
  private NestedScrollingChildHelper childHelper;
  private final int[] scrollOffset = new int[2];
  private final int[] scrollConsumed = new int[2];
  private int nestedOffsetX;
  private int nestedOffsetY;
  private int lastX;
  private int lastY;

  public NestedWebView(Context context) {
    this(context, null);
  }

  public NestedWebView(Context context, AttributeSet attrs) {
    this(context, attrs, android.R.attr.webViewStyle);
  }

  public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOverScrollMode(View.OVER_SCROLL_NEVER);
    childHelper = new NestedScrollingChildHelper(this);
    setNestedScrollingEnabled(true);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (!isNestedScrollingEnabled()) {
      return super.onTouchEvent(ev);
    }
    boolean returnValue = false;
    MotionEvent event = MotionEvent.obtain(ev);
    final int action = event.getActionMasked();
    if (action == MotionEvent.ACTION_DOWN) {
      nestedOffsetX = 0;
      nestedOffsetY = 0;
    }
    int eventX = (int) event.getX();
    int eventY = (int) event.getY();
    event.offsetLocation(nestedOffsetX, nestedOffsetY);
    switch (action) {
      case MotionEvent.ACTION_MOVE:
        int deltaX = lastX - eventX;
        int deltaY = lastY - eventY;
        // NestedPreScroll
        if (dispatchNestedPreScroll(deltaX, deltaY, scrollConsumed, scrollOffset)) {
          deltaX -= scrollConsumed[0];
          deltaY -= scrollConsumed[1];
          lastX = eventX - scrollOffset[0];
          lastY = eventY - scrollOffset[1];
          event.offsetLocation(-scrollOffset[0], -scrollOffset[1]);
          nestedOffsetX += scrollOffset[0];
          nestedOffsetY += scrollOffset[1];
        } else {
          lastX = eventX;
          lastY = eventY;
        }
        if (0 != deltaX && 0 != deltaY) {
          returnValue = super.onTouchEvent(event);
        }
        // NestedScroll
        if (dispatchNestedScroll(scrollOffset[0], scrollOffset[1], deltaX, deltaY, scrollOffset)) {
          event.offsetLocation(scrollOffset[0], scrollOffset[1]);
          nestedOffsetX += scrollOffset[0];
          nestedOffsetY += scrollOffset[1];
          lastX -= scrollOffset[0];
          lastY -= scrollOffset[1];
        }
        break;
      case MotionEvent.ACTION_DOWN:
        returnValue = super.onTouchEvent(event);
        lastX = eventX;
        lastY = eventY;
        // start NestedScroll
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL | ViewCompat.SCROLL_AXIS_HORIZONTAL);
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        returnValue = super.onTouchEvent(event);
        // end NestedScroll
        stopNestedScroll();
        break;
    }
    return returnValue;
  }

  // Nested Scroll implements
  @Override
  public void setNestedScrollingEnabled(boolean enabled) {
    childHelper.setNestedScrollingEnabled(enabled);
  }

  @Override
  public boolean isNestedScrollingEnabled() {
    return childHelper.isNestedScrollingEnabled();
  }

  @Override
  public boolean startNestedScroll(int axes) {
    return childHelper.startNestedScroll(axes);
  }

  @Override
  public void stopNestedScroll() {
    childHelper.stopNestedScroll();
  }

  @Override
  public boolean hasNestedScrollingParent() {
    return childHelper.hasNestedScrollingParent();
  }

  @Override
  public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                                      int[] offsetInWindow) {
    return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
    return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
    return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
  }

  @Override
  public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
    return childHelper.dispatchNestedPreFling(velocityX, velocityY);
  }

}
