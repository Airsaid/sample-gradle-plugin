package com.airsaid.example.custom.processor

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Register
import com.airsaid.sample.core.processor.SampleInterface

/**
 * @author airsaid
 */
@Suppress("unused")
class AlertDialogSample {

  @Register(title = "Login", desc = "A sample login prompt Dialog example.")
  class LoginDialogSample : SampleInterface<AlertDialog> {
    override fun getObject(context: AppCompatActivity): AlertDialog {
      return AlertDialog.Builder(context).apply {
        setTitle("Login Alert")
        setMessage("Are you sure, you want to continue?")
        setPositiveButton("Yes") { _, _ ->
          Toast.makeText(context, "Selected Option: YES", Toast.LENGTH_SHORT).show()
        }
        setNegativeButton("No", null)
      }.create()
    }
  }

  @Register(title = "Select color", desc = "A sample multi-select Dialog example.")
  class SelectColorDialogSample : SampleInterface<AlertDialog> {
    private val colors = arrayOf("Pink", "Red", "Yellow", "Blue")
    private val checkedItems = BooleanArray(colors.size)
    private val selectList = mutableListOf<Int>()

    override fun getObject(context: AppCompatActivity): AlertDialog {
      return AlertDialog.Builder(context).apply {
        setTitle("Choose Colors")
        setMultiChoiceItems(colors, checkedItems) { _, which, isChecked ->
          if (isChecked) {
            selectList.add(which)
          } else if (selectList.contains(which)) {
            selectList.remove(Integer.valueOf(which))
          }
        }
        setPositiveButton("Yes") { _, _ ->
          val msg = StringBuilder()
          msg.append("Total ${selectList.size} Items Selected. ")
          for (i in selectList.indices) {
            msg.append("${i + 1}: ${colors[selectList[i]]}")
          }
          Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
        }
        setNegativeButton("No", null)
      }.create()
    }
  }
}
