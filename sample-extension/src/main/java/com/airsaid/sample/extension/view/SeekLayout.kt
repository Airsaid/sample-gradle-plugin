package com.airsaid.sample.extension.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.airsaid.sample.extension.R
import com.airsaid.sample.extension.utils.SeekBarChangeListenerAdapter

/**
 * @author airsaid
 */
class SeekLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

  val titleView: TextView

  val seekBarView: SeekBar

  var title: String = ""
    @SuppressLint("SetTextI18n")
    set(value) {
      field = value
      titleView.text = "$value: $progress"
    }

  var min: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      seekBarView.min
    } else {
      0
    }
    set(value) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        seekBarView.min = value
      }
    }

  var max: Int
    get() = seekBarView.max
    set(value) {
      seekBarView.max = value
    }

  var progress: Int
    get() = seekBarView.progress
    set(value) {
      seekBarView.progress = value
    }

  var onSeekBarChangeListener: ((SeekBar, Int, Boolean) -> Unit)? = null

  init {
    orientation = VERTICAL
    inflate(context, R.layout.sample_seek_layout, this)
    titleView = findViewById(R.id.titleText)
    seekBarView = findViewById(R.id.seekBar)
    seekBarView.setOnSeekBarChangeListener(object : SeekBarChangeListenerAdapter {
      @SuppressLint("SetTextI18n")
      override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        titleView.text = "$title: $progress"
        onSeekBarChangeListener?.let { it(seekBar, progress, fromUser) }
      }
    })

    context.withStyledAttributes(attrs, R.styleable.SeekLayout, defStyleAttr, defStyleRes) {
      getString(R.styleable.SeekLayout_seek_title)?.let { title = it }
      min = getInt(R.styleable.SeekLayout_android_min, 0)
      max = getInt(R.styleable.SeekLayout_android_max, 100)
      progress = getInt(R.styleable.SeekLayout_android_progress, 0)
    }
  }
}
