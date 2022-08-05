package com.airsaid.sample.extension.component.message;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;

import com.airsaid.sample.extension.R;

/**
 * @author JackChen
 */
public class SampleSlidingLayout extends ViewGroup {
  private static final String TAG = "SampleSlidingLayout";
  private static final int HORIZONTAL = LinearLayout.HORIZONTAL;
  private static final int VERTICAL = LinearLayout.VERTICAL;

  /**
   * The maximum child view count this container could have
   */
  private final static int MAX_VIEW_COUNT = 3;
  /**
   * Center handle view's id
   */
  private int handleId = View.NO_ID;
  /**
   * Different weight between top and bottom panel
   */
  private float slidingWeight = 0f;
  /**
   * The last drag x,y point
   */
  private float lastX = 0f;
  private float lastY = 0f;
  /**
   * scaled touch slop
   */
  private int scaledTouchSlop = 0;
  /**
   * Is begin to drag
   */
  private boolean isBeingDragged = false;
  /**
   * The temp rect
   */
  private final RectF tempRect = new RectF();
  private int orientation;

  public SampleSlidingLayout(Context context) {
    this(context, null, R.attr.sampleSlidingLayout);
  }

  public SampleSlidingLayout(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.sampleSlidingLayout);
  }

  public SampleSlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setWillNotDraw(false);
    scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleSlidingLayout, defStyleAttr, R.style.Sample_SlidingLayout);
    setSlidingHandle(a.getResourceId(R.styleable.SampleSlidingLayout_sliding_handle, View.NO_ID));
    setSlidingWeight(a.getFloat(R.styleable.SampleSlidingLayout_sliding_weight, 0f));
    setOrientation(a.getInt(R.styleable.SampleSlidingLayout_android_orientation, VERTICAL));
    a.recycle();
  }

  private void setOrientation(int orientation) {
    this.orientation = orientation;
  }


  /**
   * Setup the handle view's id
   */
  private void setSlidingHandle(@IdRes int id) {
    if (id == View.NO_ID) {
      throw new NullPointerException("Before you drag the layout,you should set sliding_handle to the handle bar!");
    }
    this.handleId = id;
  }

  /**
   * Setup the top and bottom panel's split weight
   */
  private void setSlidingWeight(float weight) {
    this.slidingWeight = weight;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    //Check if handle view existed
    View handleView = findViewById(handleId);
    if (null == handleView) {
      Resources resources = getResources();
      throw new IllegalArgumentException("Handle id is invalid! Please check your id:" + resources.getResourceEntryName(handleId));
    }
  }

  @Override
  public void onViewAdded(View child) {
    super.onViewAdded(child);
    int childCount = getChildCount();
    if (childCount > MAX_VIEW_COUNT) {
      throw new IllegalArgumentException("SlidingLayout can't add additional views!");
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (HORIZONTAL == orientation) {
      measureHorizontally(widthMeasureSpec, heightMeasureSpec);
    } else if (VERTICAL == orientation) {
      measureVertically(widthMeasureSpec, heightMeasureSpec);
    }
  }

  private void measureHorizontally(int widthMeasureSpec, int heightMeasureSpec) {
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();
    int measuredWidth = getMeasuredWidth();
    //The handle view
    View handleLayout = findViewById(handleId);
    measureChild(handleLayout, widthMeasureSpec, heightMeasureSpec);

    int layoutWidth = measuredWidth - handleLayout.getMeasuredWidth() - paddingLeft - paddingRight;

    //The top panel
    View startLayout = getChildAt(0);
    //First measure the top panel
    float startLayoutWidth = slidingWeight * layoutWidth;
    startLayout.measure(MeasureSpec.makeMeasureSpec((int) startLayoutWidth, MeasureSpec.EXACTLY), heightMeasureSpec);

    //Measured the bottom view
    int childCount = getChildCount();
    View endLayout = getChildAt(childCount - 1);
    float endLayoutWidth = (1f - slidingWeight) * layoutWidth;
    endLayout.measure(MeasureSpec.makeMeasureSpec((int) endLayoutWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
  }

  private void measureVertically(int widthMeasureSpec, int heightMeasureSpec) {
    int paddingTop = getPaddingTop();
    int paddingBottom = getPaddingBottom();
    int measuredHeight = getMeasuredHeight();
    //The handle view
    View handleLayout = findViewById(handleId);
    measureChild(handleLayout, widthMeasureSpec, heightMeasureSpec);

    int layoutHeight = measuredHeight - handleLayout.getMeasuredHeight() - paddingTop - paddingBottom;

    //The top panel
    View startLayout = getChildAt(0);
    //First measure the top panel
    float startLayoutHeight = slidingWeight * layoutHeight;
    startLayout.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) startLayoutHeight, MeasureSpec.EXACTLY));

    //Measured the bottom view
    int childCount = getChildCount();
    View endLayout = getChildAt(childCount - 1);
    float endLayoutHeight = (1f - slidingWeight) * layoutHeight;
    endLayout.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) endLayoutHeight, MeasureSpec.EXACTLY));
  }


  @Override
  protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
    if (HORIZONTAL == orientation) {
      layoutHorizontally();
    } else if (VERTICAL == orientation) {
      layoutVertically();
    }
  }

  private void layoutHorizontally() {
    int measuredWidth = getMeasuredWidth();
    int measuredHeight = getMeasuredHeight();
    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int paddingRight = getPaddingRight();
    int paddingBottom = getPaddingBottom();
    View handleLayout = findViewById(handleId);
    View startLayout = getChildAt(0);

    int offsetLeft = getPaddingLeft();
    //Layout the top view
    startLayout.layout(offsetLeft, paddingTop,
        offsetLeft + startLayout.getMeasuredWidth(), measuredHeight - paddingBottom);
    offsetLeft += startLayout.getMeasuredWidth();
    //Layout the handle view
    handleLayout.layout(offsetLeft, paddingTop,
        offsetLeft + handleLayout.getMeasuredWidth(), measuredHeight - paddingBottom);
    offsetLeft += handleLayout.getMeasuredWidth();
    //Layout the bottom view
    int childCount = getChildCount();
    View endLayout = getChildAt(childCount - 1);
    endLayout.layout(offsetLeft, paddingTop,
        offsetLeft + endLayout.getMeasuredWidth(), measuredHeight - paddingBottom);
  }

  private void layoutVertically() {
    int measuredWidth = getMeasuredWidth();
    int measuredHeight = getMeasuredHeight();
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();
    int paddingBottom = getPaddingBottom();
    View handleLayout = findViewById(handleId);
    View startLayout = getChildAt(0);

    int offsetTop = getPaddingTop();
    //Layout the top view
    startLayout.layout(paddingLeft, offsetTop,
        measuredWidth - paddingRight, offsetTop + startLayout.getMeasuredHeight());
    offsetTop += startLayout.getMeasuredHeight();
    //Layout the handle view
    handleLayout.layout(paddingLeft, offsetTop,
        measuredWidth - paddingRight, offsetTop + handleLayout.getMeasuredHeight());
    offsetTop += handleLayout.getMeasuredHeight();
    //Layout the bottom view
    int childCount = getChildCount();
    View endLayout = getChildAt(childCount - 1);
    endLayout.layout(paddingLeft, offsetTop,
        measuredWidth - paddingRight, offsetTop + endLayout.getMeasuredHeight());
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    int action = ev.getActionMasked();
    if (MotionEvent.ACTION_DOWN == action) {
      lastX = ev.getX();
      lastY = ev.getY();
      if (isBeginDragged(ev)) {
        ViewParent parent = getParent();
        if (null != parent) {
          parent.requestDisallowInterceptTouchEvent(true);
        }
        return true;
      }
    } else if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
      isBeingDragged = false;
    }
    return isBeingDragged;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getActionMasked();
    if (MotionEvent.ACTION_DOWN == action) {
      lastX = event.getX();
      lastY = event.getY();
    } else if (MotionEvent.ACTION_MOVE == action) {
      //If drag the handle. start update the view
      if (isBeingDragged || isBeginDragged(event)) {
        int offsetX = (int) (event.getX() - lastX);
        int offsetY = (int) (event.getY() - lastY);
        lastX = event.getX();
        lastY = event.getY();
        View startLayout = getChildAt(0);
        View handleLayout = findViewById(handleId);
        View endLayout = getChildAt(getChildCount() - 1);
        if (HORIZONTAL == orientation) {
          offsetLayoutHorizontally(offsetX, startLayout, handleLayout, endLayout);
        } else if (VERTICAL == orientation) {
          offsetLayoutVertically(offsetY, startLayout, handleLayout, endLayout);
        }
      }
    } else if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
      isBeingDragged = false;
    }
    return true;
  }

  private void offsetLayoutHorizontally(int offsetX, View startLayout, View handleLayout, View endLayout) {
    //If move down out of bounds
    if (0 > offsetX) {
      if (handleLayout.getLeft() <= startLayout.getMinimumWidth()) {
        offsetX = 0;
      } else if (startLayout.getMinimumWidth() > handleLayout.getLeft() + offsetX) {
        offsetX = startLayout.getMinimumWidth() - handleLayout.getLeft();
      }
    }
    //If move up out of bounds
    if (0 < offsetX) {
      float endOffset = getMeasuredWidth() - endLayout.getLeft();
      if (endOffset <= endLayout.getMinimumWidth()) {
        offsetX = 0;
      } else if (endLayout.getMinimumWidth() > endOffset - offsetX) {
        offsetX = (int) (endOffset - endLayout.getMinimumWidth());
      }
    }
    //move the handle
    handleLayout.offsetLeftAndRight(offsetX);
    endLayout.offsetLeftAndRight(offsetX);
    LayoutParams layoutParams = startLayout.getLayoutParams();
    layoutParams.width = offsetX + startLayout.getMeasuredWidth();
    LayoutParams endLayoutParams = endLayout.getLayoutParams();
    endLayoutParams.width = offsetX + endLayout.getMeasuredWidth();
    //update the weight of this view
    int measuredWidth = getMeasuredWidth();
    float layoutWidth = measuredWidth - handleLayout.getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    float slidingWeight = layoutParams.width * 1f / layoutWidth;
    this.slidingWeight = slidingWeight;
    requestLayout();
  }

  private void offsetLayoutVertically(int offsetY, View startLayout, View handleLayout, View endLayout) {
    //If move down out of bounds
    if (0 > offsetY) {
      if (handleLayout.getTop() <= startLayout.getMinimumHeight()) {
        offsetY = 0;
      } else if (startLayout.getMinimumHeight() > handleLayout.getTop() + offsetY) {
        offsetY = startLayout.getMinimumHeight() - handleLayout.getTop();
      }
    }
    //If move up out of bounds
    if (0 < offsetY) {
      float endOffset = getMeasuredHeight() - endLayout.getTop();
      if (endOffset <= endLayout.getMinimumHeight()) {
        offsetY = 0;
      } else if (endLayout.getMinimumHeight() > endOffset - offsetY) {
        offsetY = (int) (endOffset - endLayout.getMinimumHeight());
      }
    }
    //move the handle
    handleLayout.offsetTopAndBottom(offsetY);
    endLayout.offsetTopAndBottom(offsetY);
    LayoutParams layoutParams = startLayout.getLayoutParams();
    layoutParams.height = offsetY + startLayout.getMeasuredHeight();
    LayoutParams endLayoutParams = endLayout.getLayoutParams();
    endLayoutParams.height = offsetY + endLayout.getMeasuredHeight();
    //update the weight of this view
    int measuredHeight = getMeasuredHeight();
    float layoutHeight = measuredHeight - handleLayout.getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    float slidingWeight = layoutParams.height * 1f / layoutHeight;
    this.slidingWeight = slidingWeight;
    requestLayout();
  }


  /**
   * Check if you touch in handle area, if the finger move offset over scale touch slop then start drag
   */
  private boolean isBeginDragged(MotionEvent ev) {
    float x = ev.getX();
    float y = ev.getY();
    View handleLayout = findViewById(handleId);
    tempRect.set(handleLayout.getLeft(), handleLayout.getTop(), handleLayout.getRight(), handleLayout.getBottom());
    if (tempRect.contains(x, y)) {
      isBeingDragged = true;
      return true;
    }
    return false;
  }
}
