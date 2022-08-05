package com.airsaid.sample.extension.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.airsaid.sample.extension.R;

/**
 * @author JackChen
 */
public class RadioLayout extends RadioGroup {

  private OnCheckedChangeListener listener;

  public RadioLayout(Context context) {
    this(context, null);
  }

  public RadioLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadioLayout);

    setRadioArray(a.getTextArray(R.styleable.RadioLayout_radio_items));
    a.recycle();
  }

  /**
   * set radio group array.
   */
  public void setRadioArray(CharSequence[] textArray) {
    removeAllViews();
    Context context = getContext();
    if (null != textArray) {
      for (int i = 0; i < textArray.length; i++) {
        CharSequence text = textArray[i];
        RadioButton appCompatCheckBox = new RadioButton(context);
        appCompatCheckBox.setId(i);
        appCompatCheckBox.setText(text);
        appCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
            if (null != listener && isSelected) {
              int index = indexOfChild(compoundButton);
              listener.onCheckedChanged(compoundButton, index);
            }
          }
        });
        addView(appCompatCheckBox);
      }
    }
  }

  public int getCheckedPosition() {
    return getCheckedRadioButtonId();
  }

  public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
    this.listener = listener;
  }

  /**
   * interface responsible for receiving compoundButton's check change event
   */
  public interface OnCheckedChangeListener {
    void onCheckedChanged(CompoundButton compoundButton, int index);
  }
}
