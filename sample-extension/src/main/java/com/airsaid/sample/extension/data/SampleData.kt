package com.airsaid.sample.extension.data

import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import kotlin.random.Random

/**
 * @author airsaid
 */
object SampleData {

  @JvmStatic
  fun getRandomColor(): Int {
    return Color.rgb(
      (Math.random() * 255).toInt(),
      (Math.random() * 255).toInt(),
      (Math.random() * 255).toInt()
    )
  }

  @JvmStatic
  fun getRandomString(): String {
    val strings = getStringArray()
    return strings[Random.nextInt(0, strings.size - 1)]
  }

  @JvmStatic
  fun getStringArray(): Array<String> {
    return Cheeses.sCheeseStrings
  }

  fun showColorPickerDialog(
    activity: FragmentActivity,
    defColor: Int = Color.BLACK,
    callback: (color: Int) -> Unit
  ) {
    ColorPickerDialog()
      .withColor(defColor)
      .withAlphaEnabled(true)
      .withListener { _, color ->
        callback(color)
      }
      .show(activity.supportFragmentManager, ColorPickerDialog::class.java.simpleName)
  }
}
