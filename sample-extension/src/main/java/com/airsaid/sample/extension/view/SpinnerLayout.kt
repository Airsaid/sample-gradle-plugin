package com.airsaid.sample.extension.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.airsaid.sample.extension.R

/**
 * @author airsaid
 */
class SpinnerLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = androidx.appcompat.R.attr.spinnerStyle,
  mode: Int = MODE_THEME
) : androidx.appcompat.widget.AppCompatSpinner(context, attrs, defStyleAttr, mode) {

  companion object {
    private const val MODE_THEME = -1
  }

  fun setData(texts: Array<String>, selectedListener: ((view: View, position: Int, text: String) -> Unit)? = null) {
    val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, texts)
    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
    setAdapter(adapter)

    selectedListener?.let { listener ->
      onItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
          listener(view, position, texts[position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
      }
    }
  }
}