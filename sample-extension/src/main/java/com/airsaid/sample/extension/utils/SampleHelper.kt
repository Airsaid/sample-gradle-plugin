package com.airsaid.sample.extension.utils

import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import kotlin.random.Random
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog

/**
 * @author airsaid
 */
object SampleHelper {

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

  @JvmStatic
  @JvmOverloads
  fun getRandomImageUrl(
    width: Int = 200,
    height: Int = 300
  ): String {
    return "https://picsum.photos/$width/$height"
  }

  @JvmStatic
  @JvmOverloads
  fun getRandomImageUrls(
    width: Int = 200,
    height: Int = 300,
    size: Int = 100
  ): List<String> {
    return mutableListOf<String>().apply {
      repeat(size) {
        add(getRandomImageUrl(width, height))
      }
    }
  }

  @JvmStatic
  @JvmOverloads
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
