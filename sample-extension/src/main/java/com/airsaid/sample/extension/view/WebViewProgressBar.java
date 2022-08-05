package com.airsaid.sample.extension.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.airsaid.sample.extension.R;

/**
 * @author JackChen
 */
public class WebViewProgressBar extends View {

  private Animator animator;
  /**
   * if user skip this animation. the progress will fast run to max automatically
   */
  private boolean passAnimation;
  private Drawable progressDrawable = null;
  private Drawable normalDrawable = null;
  private int firstProgress = 0;
  private int firstDuration = 0;
  private int firstMinDuration = 0;
  private int secondDuration = 0;
  private int secondProgress = 0;
  private int thirdDuration = 0;
  private float progress = 0f;
  private long startAnimatorTime = 0L;
  private OnProgressListener listener;
  private int max = 0;
  private int min = 0;

  public WebViewProgressBar(Context context) {
    this(context, null, R.attr.webViewProgressBar);
  }

  public WebViewProgressBar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, R.attr.webViewProgressBar);
  }

  public WebViewProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WebViewProgressBar, defStyleAttr, R.style.Sample_WebViewProgressBar);
    setProgressDrawable(a.getDrawable(R.styleable.WebViewProgressBar_web_progressDrawable));
    setNormalDrawable(a.getDrawable(R.styleable.WebViewProgressBar_web_normalDrawable));
    setFirstDuration(a.getInteger(R.styleable.WebViewProgressBar_web_firstDuration, 0));
    setFirstMinDuration(a.getInteger(R.styleable.WebViewProgressBar_web_firstMinDuration, 0));
    setSecondDuration(a.getInteger(R.styleable.WebViewProgressBar_web_secondDuration, 0));
    setFirstProgress(a.getInteger(R.styleable.WebViewProgressBar_web_firstProgress, 0));
    setSecondProgress(a.getInteger(R.styleable.WebViewProgressBar_web_secondProgress, 0));
    setThirdDuration(a.getInteger(R.styleable.WebViewProgressBar_web_thirdDuration, 0));
    setProgress(a.getFloat(R.styleable.WebViewProgressBar_web_progress, 0f));
    setMax(a.getInteger(R.styleable.WebViewProgressBar_web_max, 0));
    setMin(a.getInteger(R.styleable.WebViewProgressBar_web_min, 0));
    a.recycle();
  }

  private void setThirdDuration(int duration) {
    this.thirdDuration = duration;
  }

  public void setProgressDrawable(Drawable drawable) {
    this.progressDrawable = drawable;
    invalidate();
  }

  public void setNormalDrawable(Drawable drawable) {
    this.normalDrawable = drawable;
    invalidate();
  }

  public void setFirstDuration(int duration) {
    this.firstDuration = duration;
    invalidate();
  }

  public void setFirstMinDuration(int duration) {
    this.firstMinDuration = duration;
  }

  public void setFirstProgress(int progress) {
    this.firstProgress = progress;
  }

  public void setSecondProgress(int progress) {
    if (this.firstProgress >= progress) {
      throw new IllegalArgumentException("the progress is out of range(" + min + "," + max + ")!");
    }
    this.secondProgress = progress;
  }

  public void setSecondDuration(int duration) {
    this.secondDuration = duration;
    invalidate();
  }

  public void setMax(int max) {
    if (this.min >= max) {
      throw new IllegalArgumentException("min >= max!!");
    }
    this.max = max;
    invalidate();
  }

  public void setMin(int min) {
    if (min >= this.max) {
      throw new IllegalArgumentException("min >= max!!");
    }
    this.min = min;
    invalidate();
  }

  public void setProgress(float progress) {
    this.progress = progress;
    invalidate();
  }

  public float getProgress() {
    return progress;
  }

  public int getFirstProgress() {
    return firstProgress;
  }

  /**
   * 启动进度动画
   */
  public void startProgressAnim() {
    cancelAnimator();
    startAnimatorTime = SystemClock.uptimeMillis();
    long totalDuration = firstDuration + secondDuration + thirdDuration;
    Keyframe frame1 = Keyframe.ofFloat(0f, 0f);
    Keyframe frame2 = Keyframe.ofFloat(firstDuration * 1f / totalDuration, firstProgress);
    Keyframe frame3 = Keyframe.ofFloat((firstDuration + secondDuration) * 1f / totalDuration, secondProgress);
    Keyframe frame4 = Keyframe.ofFloat(totalDuration * 1f / totalDuration, max);
    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofKeyframe("progress", frame1, frame2, frame3, frame4));
    animator = objectAnimator;
    objectAnimator.setDuration(firstDuration + secondDuration + thirdDuration);
    objectAnimator.setInterpolator(new DecelerateInterpolator());
    objectAnimator.start();
  }

  /**
   * Finished this animation immediately
   */
  public void passAnimation() {
    if (!passAnimation) {
      passAnimation = true;
      long intervalTime = SystemClock.uptimeMillis() - startAnimatorTime;
      if (intervalTime > firstMinDuration) {
        passAnimationInner();
      } else {
        postDelayed(new Runnable() {
          @Override
          public void run() {
            passAnimationInner();
          }
        }, firstMinDuration - intervalTime);
      }
    }
  }

  /**
   * outside is load finished, here we just skip all the animation
   */
  private void passAnimationInner() {
    cancelAnimator();
    startAnimatorTime = SystemClock.uptimeMillis();
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", this.progress, max);
    animator = objectAnimator;
    objectAnimator.setInterpolator(new DecelerateInterpolator());
    objectAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (null != listener) {
          listener.onLoadFinished(WebViewProgressBar.this);
        }
      }
    });
    objectAnimator.start();
  }

  /**
   * 重置进度动画
   */
  public void resetProgressAnim() {
    progress = 0f;
    passAnimation = false;
    cancelAnimator();
    invalidate();
  }

  /**
   * 取消进度动画
   */
  public void cancelProgressAnim() {
    cancelAnimator();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int paddingTop = getPaddingTop();
    int paddingBottom = getPaddingBottom();
    int measuredWidth = getMeasuredWidth();
    int measuredHeight = 0;
    if (null != normalDrawable) {
      measuredHeight = normalDrawable.getIntrinsicHeight();
    }
    if (null != progressDrawable) {
      measuredHeight = Math.max(measuredHeight, progressDrawable.getIntrinsicHeight());
    }
    setMeasuredDimension(measuredWidth, paddingTop + measuredHeight + paddingBottom);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int width = getWidth();
    int height = getHeight();
    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int paddingRight = getPaddingRight();
    int paddingBottom = getPaddingBottom();
    float itemWidth = (width - paddingLeft - paddingRight) * 1f / max;
    if (null != normalDrawable) {
      normalDrawable.setBounds(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
      normalDrawable.draw(canvas);
    }
    if (null != progressDrawable) {
      progressDrawable.setBounds(paddingLeft, paddingTop, (int) (paddingLeft + itemWidth * progress), height - paddingBottom);
      progressDrawable.draw(canvas);
    }
  }

  /**
   * cancel an animator
   */
  private void cancelAnimator() {
    if (null != animator) {
      animator.cancel();
      animator.removeAllListeners();
      if (animator instanceof ValueAnimator) {
        ValueAnimator valueAnimator = (ValueAnimator) this.animator;
        valueAnimator.removeAllUpdateListeners();
      }
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    cancelAnimator();
    super.onDetachedFromWindow();
  }

  public void setOnProgressListener(OnProgressListener listener) {
    this.listener = listener;
  }

  /**
   * This listener responsible for the finish event
   */
  public interface OnProgressListener {
    void onLoadFinished(View v);
  }

}
