package com.airsaid.example.function

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airsaid.sample.api.Register
import com.airsaid.sample.extension.permission.SamplePermission
import com.airsaid.sample.extension.permission.SamplePermissionFunction
import com.airsaid.sample.extension.permission.addPermissionObserver
import com.airsaid.example.R

@SamplePermission(
  Manifest.permission.CAMERA,
  Manifest.permission.WRITE_EXTERNAL_STORAGE
)
@Register(title = "权限功能", desc = "演示动行时权限动态扩展功能")
class SamplePermissionActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_function_permission_sample)
    addPermissionObserver { result ->
      if (result.granted) {
        val text = getString(R.string.permission_granted, result.name)
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
      } else {
        val text = getString(R.string.permission_denied, result.name)
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
      }
    }
  }
}
